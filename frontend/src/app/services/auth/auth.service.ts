import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  public readonly server_url = 'http://10.99.134.87:8080/api';
  
  private employeeData = {role:'', employeeName:''};

  constructor(private http: HttpClient, private router: Router) { }

  login(userName: string, password: string): Observable<any>{
    return this.http.post<any>(`${this.server_url}/employees/login`, {userName, password});
  }

  setEmployeeData(role: string, employeeName: string){
    this.employeeData = {role, employeeName};
  }

  getEmployeeData(){
    return this.employeeData;
  }

  isAuthenticated(): boolean { 
    if (typeof window !== 'undefined') { 
        const token = localStorage.getItem('authToken'); 
        if (token) { 
            try {
                const payload = JSON.parse(atob(token.split('.')[1])); 
                const currentTime = Math.floor(Date.now() / 1000); 
                
                if (payload.exp && payload.exp > currentTime) {
                    this.setEmployeeData(payload.role, payload.sub); 
                    return true; 
                }
            } catch (error) {
                console.error('Invalid token format:', error);
            }
        } 
        this.router.navigate(['/login']); 
        return false; 
    } 
    return false; 
  }

  getHttpOptions(){
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const options = { headers };

    return options;
  }

  checkVehicle(data: any): boolean {
    const sqlInjectionPattern = /('|--|;|\/\*|\*\/)/;
    const xssPattern = /<script.*?>.*?<\/script>|<.*?>/;

    if(sqlInjectionPattern.test(data.description) || sqlInjectionPattern.test(data.vehicle.model) || sqlInjectionPattern.test(data.vehicle.manufacturer))
      return false;

    if(xssPattern.test(data.description) || xssPattern.test(data.vehicle.model) || xssPattern.test(data.vehicle.manufacturer))
      return false;

    if(data.description.length > 150 || data.vehicle.model.length > 150 || data.vehicle.manufacturer.length > 150)
      return false;

    return true;
  }

  checkManufacturer(data: any): boolean {
    const sqlInjectionPattern = /('|--|;|\/\*|\*\/)/;
    const xssPattern = /<script.*?>.*?<\/script>|<.*?>/;

    if(sqlInjectionPattern.test(data.name) || sqlInjectionPattern.test(data.address) || sqlInjectionPattern.test(data.country)
              || sqlInjectionPattern.test(data.telephone) || sqlInjectionPattern.test(data.email) || sqlInjectionPattern.test(data.fax))
      return false;

    if(xssPattern.test(data.name) || xssPattern.test(data.address) || xssPattern.test(data.country) || xssPattern.test(data.telephone) 
              || xssPattern.test(data.email) || xssPattern.test(data.fax))
      return false;

    if(data.name.length > 50 || data.address.length > 150 || data.country.length > 50 || data.telephone.length > 50 || data.email.length > 50)
      return false;

    return true;
  }

  checkMalfunction(data: any): boolean {
    const sqlInjectionPattern = /('|--|;|\/\*|\*\/)/;
    const xssPattern = /<script.*?>.*?<\/script>|<.*?>/;

    if(sqlInjectionPattern.test(data.description) || sqlInjectionPattern.test(data.vehicleId))
      return false;

    if(xssPattern.test(data.description) || xssPattern.test(data.vehicleId))
      return false;

    if(data.description.length > 150 || data.vehicleId.length > 150)
      return false;

    return true;
  }

  checkEmployee(data: any): boolean {
    const sqlInjectionPattern = /('|--|;|\/\*|\*\/)/;
    const xssPattern = /<script.*?>.*?<\/script>|<.*?>/;

    if(sqlInjectionPattern.test(data.userName) || sqlInjectionPattern.test(data.password) || sqlInjectionPattern.test(data.firstName)
          || sqlInjectionPattern.test(data.lastName) || sqlInjectionPattern.test(data.role))
      return false;

    if(xssPattern.test(data.userName) || xssPattern.test(data.password) || xssPattern.test(data.firstName) || xssPattern.test(data.lastName) || xssPattern.test(data.role))
      return false;

    if(data.userName.length > 50 || data.user.password.length > 250 || data.user.firstName.length > 50 || data.user.lastName.length > 50 || data.role.length > 50)
      return false;

    return true;
  }
}