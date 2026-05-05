import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-price',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './price.component.html',
  styleUrl: './price.component.css'
})
export class PriceComponent implements OnInit {
  prices: any[] = [];
  originalPrices: any[] = [];

  base_url: string = this.authService.server_url + '/rental-prices';
  notification: string | null = null;
  notificationType: string = '';  

  constructor(private authService: AuthService, private http: HttpClient){}

  ngOnInit(): void {
    this.notification = '';
    this.loadPrices();
  }

  checkIfChanged(): void {
    this.hasChanges();
  }

  hasChanges(): boolean {
    return JSON.stringify(this.prices) !== JSON.stringify(this.originalPrices);
  }

  updatePrices(): void {
    console.log("Ažurirane cijene:", this.prices);
    this.originalPrices = JSON.parse(JSON.stringify(this.prices)); 
    
    if (this.authService.isAuthenticated()) {
        const updatedContainer = { prices: this.prices };
        this.http.put<any>(this.base_url, updatedContainer, this.authService.getHttpOptions()).subscribe(
            (response) => {
              if (response && response.prices) {
                this.prices = response.prices;
                this.originalPrices = JSON.parse(JSON.stringify(this.prices));

                this.notification = 'Prices successfully updated!';
                this.notificationType = 'alert-success';
              } else {
                this.notification = 'Invalid response format!';
                this.notificationType = 'alert-danger';
              }
            },
            (error) => {
              this.notification = 'Error updating prices!';
              this.notificationType = 'alert-danger';
            }
          );
    } else {
        console.warn('User is not authenticated.');
    }
  }

  loadPrices(): void {
    if(this.authService.isAuthenticated()){
      this.http.get<any>(this.base_url, this.authService.getHttpOptions()).subscribe(
        (response) => {
          if (response && response.prices) {
            this.prices = response.prices;
            this.originalPrices = JSON.parse(JSON.stringify(this.prices)); 
          } else {
            console.error('Invalid response format');
          }
        },
        (error) => {console.error('Error loading prices:', error);}
      );
    } else {
      console.warn('User is not authenticated.');
    }
  }
}
