import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../../services/auth/auth.service';
import * as Papa from 'papaparse';
import { RentalComponent } from '../rental/rental.component';
import { MalfunctionComponent } from '../malfunction/malfunction.component';

@Component({
  selector: 'app-vehicle',
  standalone: true,
  imports: [CommonModule, NgxPaginationModule, FormsModule, RentalComponent, MalfunctionComponent],
  templateUrl: './vehicle.component.html',
  styleUrl: './vehicle.component.css'
})

export class VehicleComponent {
  server_url: any = `${this.authService.server_url}`;
  activeTab: string = 'cars';

  currentPage: number = 1;
  vehicles: any[] = [];
  filteredVehicles: any[] = [];
  selectedVehicle: any = null;
  totalPages: number = 0;
  itemsPerPage: number = 6;
  
  isModalOpen: boolean = false;
  isFormVisible: boolean = false;
  
  searchQuery: string = '';

  free_status: string = 'free';

  searchManufacturer: string = '';

  formData: any = {
    manufacturer: '',
    model: '',
    id:'',
    purchasePrice: '',
    picture: '',
    purchaseDate: '', //specificno za CAR
    description: '', //speciticno za CAR
    maxSpeed: '', // Specifično za SCOOTERS
    distanceRange: ''     // Specifično za BICYCLES
  };

  deleteId: number | null = null;
  detailsId: string | null = null;

  csvData: any[] = [];

  constructor(private http: HttpClient, private authService: AuthService) {}

  fetchVehicleDetails(id: string): void {
    const vehicle = this.vehicles.find((v: any) => v.id === id);
    if (vehicle) {
      this.selectedVehicle = vehicle;
    } else {
      console.error('Vehicle not found with ID:', id);
      alert('Vehicle not found!');
    }
  }  

  populateFormDataFromForm(inputData: any) {
    this.formData.manufacturer = inputData.manufacturer || '';
    this.formData.model = inputData.model || '';
    this.formData.purchasePrice = inputData.purchasePrice || '';

    if (this.activeTab === 'cars') {
      this.formData.id = 'C' + inputData.id || '';
      this.formData.purchaseDate = inputData.purchaseDate || '';
      this.formData.description = inputData.description || '';
      this.formData.maxSpeed = null; 
      this.formData.distanceRange = null;    
    } else if (this.activeTab === 'scooters') {
      this.formData.id = 'S' + inputData.id || '';
      this.formData.maxSpeed = inputData.maxSpeed || null;
      this.formData.purchaseDate = null;
      this.formData.description = null;
      this.formData.distanceRange = null; 
    } else if (this.activeTab === 'bicycles') {
      this.formData.id = 'B' + inputData.id || '';
      this.formData.distanceRange = inputData.distanceRange || null;
      this.formData.purchaseDate = null;
      this.formData.description = null;
      this.formData.maxSpeed = null; 
    }

    console.log('Form Data from Form:', this.formData);
  }

  handlePictureUpload(event: any) {
    const file = event.target.files[0];
  
    if (file && (file.type === 'image/jpeg' || file.type === 'image/jpg')) {
      const reader = new FileReader();
  
      reader.onload = () => {
        const base64String = (reader.result as string).split(',')[1]; // "data:image/jpeg;base64,..."
        this.formData.picture = base64String;
        console.log('Picture as Base64:', this.formData.picture);
      };
  
      reader.readAsDataURL(file);
    } else {
      alert('Invalid file format! Please upload a .jpg or .jpeg file.');
      this.formData.picture = null; 
    }
  } 

  addVehicleOnServer() {
    const url = `${this.server_url}/${this.activeTab}`;
    let data: any = null;

    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`).set('Content-Type', 'application/json');;
    const options = { headers };

    const vehicle = {
        manufacturer: this.formData.manufacturer || '', 
        model: this.formData.model || '', 
        id: this.formData.id || '', 
        purchasePrice: this.formData.purchasePrice || null, 
        status: this.free_status,
        picture: this.formData.picture
    };

    if (this.activeTab === 'cars') {
        data = {
            vehicleID: this.formData.id || '',
            description: this.formData.description || '', 
            purchaseDate: this.formData.purchaseDate || null,
            vehicle
        };
    } else if (this.activeTab === 'scooters') {
        data = {
            vehicleID: this.formData.id || '',
            maxSpeed: this.formData.maxSpeed || null,
            vehicle
        };
    } else if (this.activeTab === 'bicycles') {
        data = {
            vehicleID: this.formData.id || '',
            range: this.formData.range || null,
            vehicle
        };
    } else {
        alert('WRONG TYPE!');
        return;
    }

    if(this.authService.checkVehicle(data)){
      this.http.post(url, data, { headers: options.headers, observe: 'response', responseType: 'text' }).subscribe(
        response => {
            console.log('Full response:', response);
            console.log('Vehicle added successfully:', response.body);
            this.vehicles.push(data);

            alert('Vehicle added successfully!');
        },
        error => {
            console.error('Error adding vehicle:', error);
            alert('Failed to add vehicle.');
        }
      );
    } else
        alert('This vehicle details contains dangerous entries!');
  }

  populateFormDataFromCsv(rowData: any) {
    this.formData.manufacturer = rowData.manufacturer || '';
    this.formData.model = rowData.model || '';
    this.formData.purchasePrice = rowData.purchasePrice || '';
    this.formData.picture =  null;    

    if (this.activeTab === 'cars') {
      this.formData.id = 'C' + rowData.id || '';
      this.formData.purchaseDate = this.convertDate(rowData.purchaseDate);
      this.formData.description = rowData.description || '';
      this.formData.maxSpeed = null; 
      this.formData.distanceRange = null;    
    } else if (this.activeTab === 'scooters') {
      this.formData.id = 'S' + rowData.id || '';
      this.formData.maxSpeed = rowData.maxSpeed || null;
      this.formData.purchaseDate = null;
      this.formData.description = null;
      this.formData.distanceRange = null; 
    } else if (this.activeTab === 'bicycles') {
      this.formData.id = 'B' + rowData.id || '';
      this.formData.distanceRange = rowData.distanceRange || null;
      this.formData.purchaseDate = null;
      this.formData.description = null;
      this.formData.maxSpeed = null; 
    }

    console.log('Form Data from CSV Row:', this.formData);
  }

  convertDate(inputDate: string): string {
    const [day, month, year] = inputDate.split('-').map(Number);
    const date = new Date(year, month - 1, day); 

    return date.toISOString();
  }

  handleFileInput(event: any) {
    const file = event.target.files[0];
  
    if (file) {
      const reader = new FileReader();
  
      reader.onload = (e: any) => {
        const csvData = e.target.result;
  
        Papa.parse<any>(csvData, {
          header: true, 
          skipEmptyLines: true, 
          complete: (result) => {
            console.log('Parsirani CSV podaci:', result.data);
  
            if (result.data.length !== 1) {
              alert('CSV file must contain exactly one row of data for a vehicle!');
              return;
            }
  
            const validColumns = [
              'manufacturer',
              'model',
              'id',
              'purchasePrice',
              'purchaseDate',
              'description',
              'distanceRange',
              'maxSpeed',
            ];
  
            const csvHeaders = Object.keys(result.data[0]);
            if (csvHeaders.length !== validColumns.length || !validColumns.every(col => csvHeaders.includes(col))) {
              alert('Invalid CSV format! Please ensure the columns match the required format.');
              return;
            }
  
            const rowData = result.data[0];
            console.log('Validirani podaci:', rowData);
  
            this.populateFormDataFromCsv(rowData);

            this.finishAdding();
          },
          error: (error) => {
            console.error('Error parsing CSV file:', error);
            alert('Failed to parse the CSV file. Please check the file format.');
          }
        });
      };
  
      reader.readAsText(file);
    } else {
      alert('No file selected!');
    }
  }
  
  ngOnInit() {
    this.loadVehicles(this.currentPage);
  }

  openAddVehicleModal() {
    const modalElement = document.getElementById('addVehicleModal');
    if (modalElement) {
      modalElement.style.display = 'block';
      modalElement.classList.add('show');
    }
  }

  showForm() {
    this.isFormVisible = true;
  }
  
  submitForm() {
    console.log('Form submitted:', this.formData);
    this.populateFormDataFromForm(this.formData);
    this.finishAdding();  
  }

  finishAdding(){
    this.addVehicleOnServer();
    this.resetForm();
    this.closeModal();
  }

  resetForm() {
    this.formData = {
      manufacturer: '',
      model: '',
      id: '',
      purchasePrice: '',
      purchaseDate: '',
      description: '',
      maxSpeed: null,
      range: null
    };
  }
  
  openModal() {
    this.isFormVisible = false;
  }
  
  closeModal() {
    this.isFormVisible = false; 
    this.resetForm();
  }

  changeTab(tab: string) {
    if(this.authService.isAuthenticated()){
      this.activeTab = tab;
      this.currentPage = 1;
      this.searchQuery = '';
      this.vehicles = []; 
      this.loadVehicles(this.currentPage);
    }
  }

  loadVehicles(page: number = 1) {
    if(this.authService.isAuthenticated()){
      const url = `${this.server_url}/${this.activeTab}?page=${page}&itemsPerPage=${this.itemsPerPage}`;
    
      this.http.get<any>(url, this.authService.getHttpOptions()).subscribe(
        (response) => {
          this.vehicles = response.content;
          this.filteredVehicles = [...this.vehicles];
          this.totalPages = response.totalPages;
          this.currentPage = response.currentPage;
        },
        (error) => {
          console.error('Error loading vehicles:', error);
        }
      );
    }
  }

  createPageArray() {
    return Array(this.totalPages).fill(0);
  }  

  applyFilter(): void {
    if (!this.searchManufacturer.trim()) {
      this.filteredVehicles = [...this.vehicles];
      return;
    }
  
    this.filteredVehicles = this.vehicles.filter(record =>
      record.vehicle.manufacturer.toLowerCase().includes(this.searchManufacturer.toLowerCase())
    );
  }

  onPageChange(page: number) {
    if(this.authService.isAuthenticated()){
      if (this.currentPage !== page) {
        this.currentPage = page;
        this.filteredVehicles = [];
        this.loadVehicles(page);
      }
    }
  }

  setDeleteId(id: number): void {
    this.deleteId = id;
  }

  confirmDelete(): void {
    if (this.deleteId !== null) {
      let url = `${this.server_url}/${this.activeTab}/${this.deleteId}`;

      const token = localStorage.getItem('authToken');
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      const options = { headers };
      
      this.http.delete(url, {headers: options.headers, observe: 'response', responseType: 'text' }).subscribe({
        next: (response) => {
          this.vehicles = this.vehicles.filter(vehicle => vehicle.id !== this.deleteId);
          alert('Vehicle deleted successfully');
        },
        error: (err) => {
          console.log(err);
          alert('Error while deleting vehicle.');
        }
      });
    }
  }

  setDetailsId(id: string): void {
    this.detailsId = id;  
    this.fetchVehicleDetails(id);
  }

  triggerFileInput() {
    const fileInput = document.getElementById('csvFileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.click();
    }
  }
}
