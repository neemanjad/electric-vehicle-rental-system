import { Component } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  userName: string = '';
  password: string = '';
  notification: string = '';

  constructor(private authService: AuthService, private router: Router){}

  validateInputs(): boolean {
    const sqlInjectionPattern = /('|--|;|\/\*|\*\/)/;
    const xssPattern = /<script.*?>.*?<\/script>|<.*?>/;
  
    if (!this.userName.trim() || !this.password.trim()) {
      this.setNotification('Username and password are required!');
      return false;
    }
  
    if (this.password.length < 6) {
      this.setNotification('Password must be at least 6 characters long!');
      return false;
    }
  
    if (sqlInjectionPattern.test(this.userName) || sqlInjectionPattern.test(this.password)) {
      this.setNotification('The entry contains potential SQL Injection characters!');
      return false;
    }
  
    if (xssPattern.test(this.userName) || xssPattern.test(this.password)) {
      this.setNotification('The entry contains a potential XSS attack!');
      return false;
    }
  
    if (this.userName.length > 50 || this.password.length > 50) {
      this.setNotification('The input is too long and may cause a buffer overflow!');
      return false;
    }
  
    return true;
  }
  
  setNotification(message: string): void {
    this.notification = message;
    this.resetForm();
  }

  resetForm(): void {
    this.userName = '';
    this.password = '';
  }
  
  onSubmit(): void {
    if(!this.validateInputs())
      return;

    this.authService.login(this.userName, this.password).subscribe({
      next: (response) => {
        if (response.role !== null && response.jsonToken !== null) {
          localStorage.setItem("authToken", response.jsonToken);
          this.authService.setEmployeeData(response.role, JSON.parse(atob(response.jsonToken.split('.')[1])).sub);
          this.router.navigate(['/dashboard']);
        } else {
          this.setNotification('Invalid role&token received!');
        }
      },
      error: () => {
        this.setNotification('Login failed! Please check your credentials.');
      },
    });
  }
}
