import { AuthService } from './../../services/auth/auth.service';
import { Component, Input, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms'; 

@Component({
  selector: 'app-rental',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './rental.component.html',
  styleUrl: './rental.component.css'
})
export class RentalComponent {
  @Input() vehicleId!: string; 
  @Input() rentalParameter: any; 

  rentals: any[] = [];
  filteredRentals: any[] = [];

  totalPages: number = 0;
  itemsPerPage = 6; 
  currentPage = 1;
  totalPagesArray: number[] = [];
  
  role: string = '';
  searchRental: string = '';

  constructor(private http: HttpClient, private authService: AuthService) {}

  ngOnInit(){
    if(this.authService.isAuthenticated()){
      this.role = this.authService.getEmployeeData().role;
      if(this.role === 'operator' || this.role === 'manager')
        this.loadRentals(this.currentPage);
    }
  }

  applyFilter(): void {
    if (!this.searchRental.trim()) {
      this.filteredRentals = [...this.rentals];
      return;
    }

    this.filteredRentals = this.rentals.filter(record =>
      record.client.userName.toLowerCase().includes(this.searchRental.toLowerCase())
    );
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['vehicleId']) {
      this.loadRentals(this.currentPage); 
    }
  }

  loadRentals(page: number = 1): void {
    if (this.authService.isAuthenticated()) {
      let url = '';
      if(this.rentalParameter === 'menuChooice')
        url = `${this.authService.server_url}/rentals?page=${page}&size=${this.itemsPerPage}`;
      else
        url = `${this.authService.server_url}/rentals/${this.vehicleId}?page=${page}&size=${this.itemsPerPage}`;
      
      this.http.get<any>(url, this.authService.getHttpOptions()).subscribe(
        (response) => {
          this.rentals = response.content;
          this.filteredRentals = [...this.rentals];
          this.totalPages = response.totalPages;
          this.currentPage = response.currentPage;
          this.totalPagesArray = Array(this.totalPages).fill(0).map((_, i) => i + 1);
        },
        (error) => {
          console.error('Error loading rentals:', error);
        }
      );
    } else {
      console.warn('User is not authenticated.');
    }
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.loadRentals(page);
    }
  }
}
