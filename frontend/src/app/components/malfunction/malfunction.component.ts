import { Component, Input, SimpleChanges } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../../services/auth/auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-malfunction',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './malfunction.component.html',
  styleUrls: ['./malfunction.component.css'],
})
export class MalfunctionComponent {
  @Input() vehicleId!: string; 

  malfunctions: any[] = [];
  filteredMalfunctions: any[] = [];

  totalPages = 0;
  currentPage = 1;
  itemsPerPage = 6;
  
  deleteId: number | null = null; 
  isDeleteModalOpen = false; 
  isAddModalOpen = false; 
  
  role: string = '';
  searchDescription: string = '';

  newMalfunction = {
    idMalfunction: '',
    description: '',
    repairCosts: '',
    malfunctionDate: '',
    repairDate: '',
    vehicleId: '',
  };

  constructor(private http: HttpClient, private authService: AuthService) {}

  ngOnInit(){
    if(this.authService.isAuthenticated()){
      let employeeData = this.authService.getEmployeeData();
      this.role = employeeData.role;
    }
  }

  applyFilter(): void {
    if (!this.searchDescription.trim()) {
      this.filteredMalfunctions = [...this.malfunctions];
      return;
    }
  
    this.filteredMalfunctions = this.malfunctions.filter(record =>
      record.description.toLowerCase().includes(this.searchDescription.toLowerCase())
    );
  }

  submitMalfunction(myForm: any) {
    if (myForm.valid) {
      const headers = this.getHeaders();
      const data = {
        idMalfunction: '', 
        description: myForm.value.description,
        repairCosts: myForm.value.repairCosts,
        malfunctionDate: myForm.value.malfunctionDate,
        repairDate: '',
        vehicleId: myForm.value.vehicleId,
      };

      if (data.malfunctionDate < new Date()) {
        alert("Malfunction date is in the past. Repair date will be set to null.");
        return;
      }

      if(!this.authService.checkMalfunction(data)){
        alert('This malfunction details contains dangerous entries!');
        return;
      }
      
      this.http.post(`${this.authService.server_url}/malfunctions`, data, { headers }).subscribe({
        next: () => {
          alert('Malfunction successfully added!');
        },
        error: (err) => {
          console.error('Failed to add malfunction', err);
        },
      });
    } else {
      console.error('Invalid form!');
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['vehicleId']?.currentValue && this.authService.isAuthenticated()) {
      this.fetchMalfunctions();
    }
  }

  fetchMalfunctions(): void {
    const url = `${this.authService.server_url}/malfunctions/${this.vehicleId}?page=${this.currentPage}&itemsPerPage=${this.itemsPerPage}`;
    const headers = this.getHeaders();

    this.http.get<any>(url, { headers }).subscribe({
      next: (response) => {
        console.log(response.content);
        this.malfunctions = response.content;
        this.filteredMalfunctions = [...this.malfunctions];
        this.totalPages = response.totalPages;
      },
      error: (err) => {
        console.error('Failed to fetch malfunctions', err);
      },
    });
  }

  goToPage(page: number): void {
    if (page > 0 && page <= this.totalPages) {
      this.currentPage = page;
      this.fetchMalfunctions();
    }
  }

  setDeleteId(id: number): void {
    this.deleteId = id;
    this.isDeleteModalOpen = true; 
  }

  deleteMalfunction(): void {
    if (this.deleteId) {
      const url = `${this.authService.server_url}/malfunctions/${this.deleteId}`;
      const headers = this.getHeaders();

      this.http.delete(url, { headers }).subscribe({
        next: () => {
          this.malfunctions = this.malfunctions.filter((m) => m.idMalfunction !== this.deleteId);
          this.deleteId = null;
          this.isDeleteModalOpen = false;
          alert('Malfunction sucessfully deleted!');
        },
        error: (err) => {
          console.error('Failed to delete malfunction', err);
          alert('Failed to delete malfunction');
        },
      });
    }
  }

  openAddModal(): void {
    this.isAddModalOpen = true;
  }

  closeAddModal(): void {
    this.isAddModalOpen = false;
    this.resetNewMalfunction();
  }

  addMalfunction(): void {
    this.newMalfunction.vehicleId = this.vehicleId; 
    let data = this.newMalfunction;
    const headers = this.getHeaders();
    this.http.post(`${this.authService.server_url}/malfunctions`, this.newMalfunction, { headers }).subscribe({
      next: () => {
        this.closeAddModal();
        this.malfunctions.push(data);
      },
      error: (err) => {
        console.log(err);
        console.error('Failed to add malfunction', err);
      },
    });
  }

  resetNewMalfunction(): void {
    this.newMalfunction = {
      idMalfunction: '',
      description: '',
      repairCosts: '',
      malfunctionDate: '',
      repairDate: '',
      vehicleId: '',
    };
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`).set('Content-Type', 'application/json');
  }
}
