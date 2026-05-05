import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { CommonModule } from '@angular/common';
import { VehicleComponent } from "../vehicle/vehicle.component";
import { MalfunctionComponent } from "../malfunction/malfunction.component";
import { RentalComponent } from "../rental/rental.component";
import { UserComponent } from "../user/user.component";
import { ManufacturerComponent } from "../manufacturer/manufacturer.component";
import { MapComponent } from "../map/map.component";
import { PriceComponent } from "../price/price.component";
import { StatisticComponent } from '../statistic/statistic.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, VehicleComponent, MalfunctionComponent, RentalComponent, 
    UserComponent, ManufacturerComponent, MapComponent, PriceComponent, StatisticComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
    employeeName: string = '';
    role: string = '';
    selectedOption: string = '';

    constructor(private router: Router, private authService: AuthService){}
  
    setOption(option: string){
      this.selectedOption = option;
    }

    ngOnInit(): void {
      if(this.authService.isAuthenticated()){
        const employeeData = this.authService.getEmployeeData();
        if(employeeData !== null){
          this.startClock();
          this.employeeName = employeeData.employeeName;
          this.role = employeeData.role;
        } else
            this.router.navigate(['/login']);
      } 
    }
    
    logout(): void{
      localStorage.removeItem('authToken');
      this.router.navigate(['/login']);
    }
  
    startClock(): void {
      const currentTimeElement = document.getElementById('currentTime');
      setInterval(() => {
        const now = new Date();
        const timeString = now.toLocaleTimeString('en-US', { hour12: false });
        if (currentTimeElement) {
          currentTimeElement.textContent = timeString;
        }
      }, 1000);
    }
}
