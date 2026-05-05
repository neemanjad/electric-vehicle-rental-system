import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormGroup, FormBuilder, ReactiveFormsModule  } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../../services/auth/auth.service';

declare var bootstrap: any; // za Bootstrap modale

@Component({
  selector: 'app-manufacturer',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './manufacturer.component.html',
  styleUrl: './manufacturer.component.css'
})
export class ManufacturerComponent implements OnInit {
  manufacturers: any[] = [];
  filteredManufacturers: any[] = [];

  searchManufacturer: string = '';

  currentPage: number = 1;
  itemsPerPage: number = 6;
  totalPages: number = 0;
  isEditMode: any = false;
  selectedManufacturer: any = null;
  base_url: string = `${this.authService.server_url}/manufacturers`;

  manufacturerForm: FormGroup; // Reactive form za unos i ažuriranje

  constructor(private http: HttpClient, private authService: AuthService, private fb: FormBuilder) {
    this.manufacturerForm = this.fb.group({
      name: [''],
      address: [''],
      country: [''],
      telephone: [''],
      fax: [''],
      email: ['']
    });
  }

  applyFilter(): void {
    if (!this.searchManufacturer.trim()) {
      this.filteredManufacturers = [...this.manufacturers];
      return;
    }
  
    this.filteredManufacturers = this.manufacturers.filter(record =>
      record.manufacturer.name.toLowerCase().includes(this.searchManufacturer.toLowerCase())
    );
  }

  ngOnInit(){
    if(this.authService.isAuthenticated())
      this.loadManufacturers(this.currentPage);
  }

  loadManufacturers(page: number = 1){
    const url = `${this.base_url}?page=${page}&size=${this.itemsPerPage}`;

    if(this.authService.isAuthenticated()){
      this.http.get<any>(url, this.authService.getHttpOptions()).subscribe(
        (response) => {
          this.manufacturers = response.content || [];
          this.filteredManufacturers = [...this.manufacturers];
          this.totalPages = response.totalPages || 0;
          this.currentPage = response.currentPage;
        },
        (error) => {
          console.error('Error loading manufacturers:', error);
        }
      );
    }
  }

  onSubmit(): void {
    const data = {
      name: this.manufacturerForm.get('name')?.value,
      address: this.manufacturerForm.get('address')?.value,
      country: this.manufacturerForm.get('country')?.value,
      telephone: this.manufacturerForm.get('telephone')?.value,
      email: this.manufacturerForm.get('email')?.value,
      fax: this.manufacturerForm.get('fax')?.value,
    };

    if(!this.authService.checkManufacturer(data)){
      alert('This manufacturer details contains dangerous entries!');
      return;
    }

    const options = this.authService.getHttpOptions();
  
    if (this.isEditMode) {
      this.http.put<any>(`${this.base_url}/${this.selectedManufacturer.manufacturer.name}`, data, { headers: options.headers, observe: 'response', responseType: 'text' as 'json' }).subscribe(
        (response) => {
          if (response.status === 200) {
            console.log('Manufacturer successfully modified!:', response.body);
            alert('Manufacturer successfully modified!');
            this.loadManufacturers(this.currentPage); 
          } else {
              console.warn('Neočekivan status odgovora:', response.status);
          }
        },
        (error) => {
          console.error('Greška prilikom obrade zahteva:', error);
          alert('An error occurred while modifying the resource. Try again.');
        }
      );
    } else {
      this.http.post<any>(`${this.base_url}`, data, { headers: options.headers, observe: 'response', responseType: 'text' as 'json' }).subscribe(
        (response) => {
          if (response.status === 201) {
            console.log('Uspješno kreiran resurs:', response.body);
            alert('Manufacturer successfully created!');
            this.loadManufacturers(this.currentPage + 1);
          } else {
              console.warn('Neočekivan status odgovora:', response.status);
          }
        },
        (error) => {
          console.error('Greška prilikom obrade zahteva:', error);
          alert('An error occurred while creating the resource. Try again.');
        }
      );
    }
    this.closeModal(); 
  }

  closeModal() {
    const modalElement = document.getElementById('employeeModal');
    const modal = bootstrap.Modal.getInstance(modalElement);
    modal?.hide();
  }

  addManufacturer(): void {
    this.isEditMode = false; 
    this.manufacturerForm.reset();
    this.openModal();
  }

  openModal() {
    const modalElement = document.getElementById('manufacturerModal');
    const modal = new bootstrap.Modal(modalElement);
    modal.show();
  }

  updateManufacturer(item: any): void {
    this.isEditMode = true;
    this.selectedManufacturer = item;
    this.manufacturerForm.patchValue({
      name: item.manufacturer.name,
      address: item.manufacturer.address,
      country: item.manufacturer.country,
      telephone: item.manufacturer.telephone,
      fax: item.manufacturer.fax,
      email: item.manufacturer.email,
    });
    this.openModal();
  }

  deleteManufacturer(item: any): void {
    const options = this.authService.getHttpOptions();
    const url = `${this.base_url}/${item.manufacturer.name}`; 
    
    if (confirm(`Are you sure you want to delete ${item.manufacturer.name} ?`)) { 
      this.http.delete(url, { headers: options.headers, observe: 'response', responseType: 'text' }).subscribe( 
        () => { 
          alert(`${item.manufacturer.name} has been deleted.`); 
          this.loadManufacturers(this.currentPage);
        }, 
        (error) => { 
          console.error('Error deleting employee:', error); 
        } 
      ); 
    } 
  }

  onPageChange(page: number) {
    if(this.authService.isAuthenticated()){
      if (this.currentPage !== page) {
        this.currentPage = page;
        this.filteredManufacturers = [];
        this.loadManufacturers(this.currentPage);
      }
    }
  }

  createPageArray() {
    return Array(this.totalPages).fill(0);
  } 
}
