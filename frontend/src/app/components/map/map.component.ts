import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './map.component.html',
  styleUrl: './map.component.css'
})
export class MapComponent implements OnInit {
  base_url = `${this.authService.server_url}/rentals`;

  rentals: any[] = [];
  currentRentalIndex = 0;
  totalPages: number = 0;
  currentPage = 1;
  itemsPerPage = 6;

  rows = 15;
  cols = 35;
  matrix: string[][] = [];
  vehiclePosition = { x: 0, y: 0 };
  path: { x: number, y: number }[] = [];
  
  index = 0;
  isAnimating = false;
  intervalMove: any;

  constructor(private http: HttpClient, private authService: AuthService) {}

  ngOnInit(): void {
    this.createMatrix();
    this.loadRentals();
  }

  createMatrix(): void {
    this.matrix = Array.from({ length: this.rows }, () =>
      Array(this.cols).fill('cell')
    );
  }

  loadRentals(page: number = 1): void {
    if(this.authService.isAuthenticated()){
      this.http.get<any>(`${this.base_url}?page=${page}&size=${this.itemsPerPage}`, this.authService.getHttpOptions()).subscribe(
        (response) => {
          this.rentals = response.content;
          this.totalPages = response.totalPages;
          this.currentPage = response.currentPage;
          this.currentRentalIndex = 0;
  
          if (this.rentals.length > 0) {
            this.startSimulation();
          }
        },
        (error) => {
          console.error('Error loading rental:', error);
        }
      );
    }
  }

  startSimulation(): void {
    if (this.isAnimating || this.currentRentalIndex >= this.rentals.length) 
      return;
    
    this.createMatrix(); 
    this.isAnimating = true;
    this.index = 0;
    this.generatePath();
    this.markStartEnd();
    this.runAnimation();
  }

  restartSimulation(): void {
    clearInterval(this.intervalMove);
    this.isAnimating = false;
    this.index = 0;
    this.startSimulation();
  }

  loadNextPage(): void {
    this.currentPage++;
    this.loadRentals(this.currentPage);
  }

  generatePath(): void {
    const rental = this.rentals[this.currentRentalIndex];
    this.path = [];

    for (let x = rental.startX; x !== rental.endX; x += rental.startX < rental.endX ? 1 : -1) {
      this.path.push({ x, y: rental.startY });
    }

    for (let y = rental.startY; y !== rental.endY; y += rental.startY < rental.endY ? 1 : -1) {
      this.path.push({ x: rental.endX, y });
    }

    this.path.push({ x: rental.endX, y: rental.endY });
  }

  markStartEnd(): void {
    const rental = this.rentals[this.currentRentalIndex];

    if (this.isInBounds(rental.startX, rental.startY)) {
      this.matrix[rental.startX][rental.startY] = 'start';
    }

    if (this.isInBounds(rental.endX, rental.endY)) {
      this.matrix[rental.endX][rental.endY] = 'end';
    }
  }

  runAnimation(): void {
    if (this.path.length === 0) return;
    const start = this.path[0];

    this.intervalMove = setInterval(() => {
      if (this.index > 0) {
        const prev = this.path[this.index - 1];
        if(prev === start){
          this.matrix[start.x][start.y] = 'start';
        } else if (this.isInBounds(prev.x, prev.y) && this.matrix[prev.x][prev.y] === 'active') {
          this.matrix[prev.x][prev.y] = 'cell'; // Resetuj samo ako nije start
        }
      }

      if (this.index < this.path.length) {
        const current = this.path[this.index];
        this.vehiclePosition = current;

        if (this.isInBounds(current.x, current.y)) {
          this.matrix[current.x][current.y] = 'active'; // PRVO x, pa y
        }

        this.index++;
      } else {
        clearInterval(this.intervalMove);
        this.isAnimating = false;
        this.currentRentalIndex++;

        if (this.index === this.path.length) {
          const last = this.path[this.path.length - 1];
          if (this.isInBounds(last.x, last.y)) {
            this.matrix[last.x][last.y] = 'active'; // PRVO x, pa y
          }
        }

        if (this.currentRentalIndex < this.rentals.length) {
          this.startSimulation();
        }
      }
    }, 300);
  }

  isInBounds(x: number, y: number): boolean {
    return x >= 0 && x < this.rows && y >= 0 && y < this.cols;
  }
}
