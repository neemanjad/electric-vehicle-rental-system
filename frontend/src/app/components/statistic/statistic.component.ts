import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth/auth.service';
import { Chart, BarController, BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend } from 'chart.js';

declare var bootstrap: any;
Chart.register(BarController, BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend);

export interface Vehicle {
  id: string;
  picture: string;
  model: string;
  manufacturer: string;
}

export interface MalfunctionRecordDTO {
  vehicle: Vehicle;
  malfunctionCount: number;
}

@Component({
  selector: 'app-statistic',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './statistic.component.html',
  styleUrl: './statistic.component.css'
})

export class StatisticComponent implements OnInit {
  selectedTab = 'incomeByDays';
  selectedImage: string = '';

  selectedMonth: string = '';
  months: string[] = [];
  dailyRevenue: { day: number, income: number }[] = [];
    
  filteredMalfunctions: MalfunctionRecordDTO[] = [];
  originalMalfunctions: MalfunctionRecordDTO[] = [];
  searchManufacturer: string = '';

  chart: any;

  vehicleIncomeData: { type: string, income: number }[] = [];

  currentPage: number = 0;
  totalPages: number = 0;
  itemsPerPage: number = 8;

  constructor(private authService: AuthService, private http: HttpClient){}

  ngOnInit(): void {
    this.loadAvailableMonths();
  }

  createChart() {
    if (this.chart) this.chart.destroy();

    this.chart = new Chart("revenueChart", {
      type: 'bar',
      data: {
        labels: this.dailyRevenue.map(entry => entry.day.toString()),
        datasets: [{
          label: 'INCOME BY DAYS ($)',
          data: this.dailyRevenue.map(entry => entry.income),
          backgroundColor: 'rgba(54, 162, 235, 0.6)',
          borderColor: 'rgba(54, 162, 235, 1)',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        scales: {
          x: { type: 'category' },
          y: { type: 'linear', beginAtZero: true }
        }
      }
    });
  }

  onTabChange(tab: string): void {
    this.selectedTab = tab;
    
    if (tab === 'failuresByVehicle') {
      this.loadMalfunctions(this.currentPage);
    } else if(tab === 'incomeByType'){
      this.loadVehicleIncomeData();
    } else if(tab === 'incomeByDays'){
      this.loadAvailableMonths();
    }
  }

  loadVehicleIncomeData(): void {
    if (this.authService.isAuthenticated()) {
    this.http.get<{ [key: string]: number }>(`${this.authService.server_url}/rentals/revenueByType`, this.authService.getHttpOptions()).subscribe(
      data => {
        this.vehicleIncomeData = Object.entries(data).map(([type, income]) => ({ type, income }));
        this.createVehicleIncomeChart();
      },
      error => console.error('Error loading revenue by vehicle type:', error));
  }
  }

  createVehicleIncomeChart(): void {
    if (this.chart) this.chart.destroy();

    this.chart = new Chart("vehicleIncomeChart", {
      type: 'bar',
      data: {
        labels: this.vehicleIncomeData.map(entry => entry.type),
        datasets: [{
          label: 'INCOME BY VEHICLE TYPE ($)',
          data: this.vehicleIncomeData.map(entry => entry.income),
          backgroundColor: ['rgba(255, 99, 132, 0.6)', 'rgba(54, 162, 235, 0.6)', 'rgba(255, 206, 86, 0.6)'],
          borderColor: ['rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)', 'rgba(255, 206, 86, 1)'],
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        scales: {
          x: { type: 'category' },
          y: { type: 'linear', beginAtZero: true }
        }
      }
    });
  }

  loadMalfunctions(page: number = 0) {
    if (this.authService.isAuthenticated()) {
      this.http.get<any>(`${this.authService.server_url}/malfunctions/all?page=${page}&itemsPerPage=${this.itemsPerPage}`, this.authService.getHttpOptions())
        .subscribe(data => {
          this.originalMalfunctions = data.content;
          this.filteredMalfunctions = [...this.originalMalfunctions];
          this.totalPages = data.totalPages;
          this.currentPage = data.currentPage;
        });
    }
  }

  applyFilter(): void {
    if (!this.searchManufacturer.trim()) {
      this.filteredMalfunctions = [...this.originalMalfunctions];
      return;
    }
  
    this.filteredMalfunctions = this.originalMalfunctions.filter(record =>
      record.vehicle.manufacturer.toLowerCase().includes(this.searchManufacturer.toLowerCase())
    );
  }

  openModal(imageUrl: string): void {
    this.selectedImage = imageUrl;
    const modal = new bootstrap.Modal(document.getElementById('imageModal'));
    modal.show();
  }

  loadRevenueData() {
    if (!this.selectedMonth) return;

    if(this.authService.isAuthenticated()){
      const encodedMonthYear = encodeURIComponent(this.selectedMonth);
      this.http.get<{ [key: number]: number }>(`${this.authService.server_url}/rentals/revenue/${encodedMonthYear}`, this.authService.getHttpOptions()).subscribe(
        (data) => {
          this.dailyRevenue = Object.entries(data).map(([day, income]) => ({ day: Number(day), income }));
          this.createChart();
        },
        (error) => console.error('Error loading revenue by day:', error)
      );
    }
  }

  loadAvailableMonths() {
    if(this.authService.isAuthenticated()){
      this.http.get<string[]>(`${this.authService.server_url}/rentals/available-months`, this.authService.getHttpOptions()).subscribe(
      (data) => {
        this.months = data;
      },
      (error) => console.error('Error loading available months:', error)
      );
    }
  }
}
