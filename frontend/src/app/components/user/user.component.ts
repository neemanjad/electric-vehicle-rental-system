import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';
import { FormGroup, FormBuilder, ReactiveFormsModule  } from '@angular/forms';

declare var bootstrap: any; 

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent implements OnInit {

  role: string = '';
  detailsId: string = '';
  searchUser: string = '';

  isEditMode = false; 

  userList: any[] = [];
  filteredUsers: any[] = [];
  employeeForm: FormGroup; 

  activeTab: string = 'clients'; 
  selectedUser: any = null;
  currentPage: number = 1;
  itemsPerPage: number = 6;
  totalPages: number = 0;

  private clientsUrl = `${this.authService.server_url}/clients`;
  private employeesUrl = `${this.authService.server_url}/employees`;

  constructor(private http: HttpClient, private authService: AuthService, private fb: FormBuilder) {
    this.employeeForm = this.fb.group({
      firstName: [''],
      lastName: [''],
      userName: [''],
      email: [''],
      password: [''],
      isBlocked: [false], 
      role: ['']
    });
  }

  onSubmit() {
    const data = {
      role: this.employeeForm.get('role')?.value,
      userName: this.employeeForm.get('userName')?.value,
      user: {
        userName: this.employeeForm.get('userName')?.value,
        firstName: this.employeeForm.get('firstName')?.value,
        lastName: this.employeeForm.get('lastName')?.value,
        password: this.employeeForm.get('password')?.value,
        isBlocked: Boolean(this.employeeForm.get('isBlocked')?.value)
      }
    };

    if(!this.authService.checkEmployee(data)){
      alert('This employee details contains dangerous entries!');
      return;
    }

    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const options = { headers };
  
    if (this.isEditMode) {
      this.http.put<any>(`${this.employeesUrl}/${this.selectedUser.userName}`, data, { headers: options.headers, observe: 'response', responseType: 'text' as 'json' }).subscribe(
        (response) => {
          if (response.status === 200) {
            console.log('Uspješno izmijenjen resurs:', response.body);
            this.loadEmployees(this.currentPage); 
          } else {
              console.warn('Neočekivan status odgovora:', response.status);
          }
        },
        (error) => {
          console.error('Greška prilikom obrade zahteva:', error);
          alert('Došlo je do greške prilikom izmjene resursa. Pokušajte ponovo.');
        }
      );
    } else {
      this.http.post<any>(this.employeesUrl, data, { headers: options.headers, observe: 'response', responseType: 'text' as 'json' }).subscribe(
        (response) => {
          if (response.status === 201) {
            alert(response.body);
            console.log('Uspješno kreiran resurs:', response.body);
            this.loadEmployees(this.currentPage);
          } else {
              console.warn('Neočekivan status odgovora:', response.status);
          }
        },
        (error) => {
          console.error('Greška prilikom obrade zahteva:', error);
          alert('Došlo je do greške prilikom kreiranja resursa. Pokušajte ponovo.');
        }
      );
    }
    this.closeModal(); 
  }

  changeTab(tab: string): void {
    if (this.activeTab !== tab && this.authService.isAuthenticated()) {
      this.activeTab = tab;
      if (this.activeTab === 'clients') {
        this.currentPage = 1;
        this.loadClients(this.currentPage);
      } else if (this.activeTab === 'employees') {
        this.currentPage = 1;
        this.loadEmployees(this.currentPage);
      }
    }
  }

  closeModal() {
    const modalElement = document.getElementById('employeeModal');
    const modal = bootstrap.Modal.getInstance(modalElement);
    modal?.hide();
  }
  
  createPageArray() {
    return Array(this.totalPages).fill(0);
  } 

  onPageChange(page: number) {
    if(this.authService.isAuthenticated()){
      if (this.currentPage !== page) {
        this.currentPage = page;
        this.filteredUsers = []; 
        this.activeTab == 'clients' ? this.loadClients(this.currentPage) : this.loadEmployees(this.currentPage);
      }
    }
  }

  applyFilter(): void {
    if (!this.searchUser.trim()) {
      this.filteredUsers = [...this.userList];
      return;
    }
  
    this.filteredUsers = this.userList.filter(record =>
      record.user.firstName.toLowerCase().includes(this.searchUser.toLowerCase())
    );
  }

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.role = this.authService.getEmployeeData().role;
      this.loadClients();
    }
  }

  loadClients(page: number = this.currentPage): void {
    const url = `${this.clientsUrl}?page=${page}&size=${this.itemsPerPage}`;

    this.http.get<any>(url, this.authService.getHttpOptions()).subscribe(
      (response) => {
        this.userList = response.content || [];
        this.filteredUsers = [...this.userList];
        this.totalPages = response.totalPages || 0;
        this.currentPage = response.currentPage;
      },
      (error) => {
        console.error('Error loading clients:', error);
      }
    );
  }

  loadEmployees(page: number = this.currentPage): void {
    const url = `${this.employeesUrl}?page=${page}&size=${this.itemsPerPage}`;

    this.http.get<any>(url, this.authService.getHttpOptions()).subscribe(
      (response) => {
        this.userList = response.content || [];
        this.filteredUsers = [...this.userList];
        this.totalPages = response.totalPages || 0;
        this.currentPage = response.currentPage;
      },
      (error) => {
        console.error('Error loading employees:', error);
      }
    );
  }

  openBlockModal(user: any) {
    this.selectedUser = user;
    const modalElement = document.getElementById('blockUserModal');
    const modal = new bootstrap.Modal(modalElement);
    modal.show();
  }

  confirmBlockAction() {
    if (!this.selectedUser) return;

    const base_url = this.activeTab === 'clients' ? this.clientsUrl : this.employeesUrl;
    const action = this.selectedUser.user.isBlocked ? 'unblock' : 'block';

    const url = `${base_url}/${action}/${this.selectedUser.userName}`;

    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const options = { headers };

    this.http.put(url, {}, { headers: options.headers, observe: 'response', responseType: 'text' }).subscribe({
      next: (response) => {
        this.selectedUser.isBlocked = !this.selectedUser.isBlocked;
        this.closeBlockModal();
        alert('Successfully ' + action + 'ed ' + this.selectedUser.user.firstName + ' ' + this.selectedUser.user.lastName);
        this.activeTab === 'clients' ? this.loadClients(this.currentPage + 1) : this.loadEmployees(this.currentPage + 1);
      },
      error: err => {
        console.error('Greška pri slanju zahteva:', err);
      }
    });
  }

  closeBlockModal() {
    const modalElement = document.getElementById('blockUserModal');
  
    if (modalElement) {
      const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
  
      if (modal) {
        modal.hide();
      } else {
        console.warn("Modal nije pravilno instanciran!");
      }
    } else {
      console.error("Element sa ID-jem 'blockUserModal' nije pronađen!");
    }
  }

  addEmployee() {
    this.isEditMode = false; 
    this.employeeForm.reset();
    this.openModal();
  }

  updateEmployee(item: any) {
    this.isEditMode = true; 
    this.selectedUser = item; 
    this.employeeForm.patchValue({
      firstName: item.user.firstName,
      lastName: item.user.lastName,
      userName: item.user.userName,
      password: '', 
      isBlocked: item.user.isBlocked,
      role: item.role
    });
    this.openModal();
  }

  openModal() {
    const modalElement = document.getElementById('employeeModal');
    const modal = new bootstrap.Modal(modalElement);
    modal.show();
  }

  deleteEmployee(employee: any): void { 
    const token = localStorage.getItem('authToken'); 
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`); 
    const options = { headers };

    const url = `${this.employeesUrl}/${employee.userName}`; 
    
    if (confirm(`Are you sure you want to delete ${employee.user.firstName} ${employee.user.lastName}?`)) { 
      this.http.delete(url, { headers: options.headers, observe: 'response', responseType: 'text' }).subscribe( 
        () => { 
          alert(`${employee.user.firstName} ${employee.user.lastName} has been deleted.`); 
          this.loadEmployees(this.currentPage + 1);
        }, 
        (error) => { 
          console.error('Error deleting employee:', error); 
        } 
      ); 
    } 
  }
}
