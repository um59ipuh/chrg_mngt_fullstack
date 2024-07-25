import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../../services/api/api.service';
import { NotificationService } from '../../../services/notification/notification.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {API_ENDPOINTS} from "../../../constants/api-endpoints";


@Component({
  selector: 'app-api',
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent implements OnInit {
  loginForm: FormGroup;
  registerForm: FormGroup;
  constructor(private fb: FormBuilder, private router: Router, private apiService: ApiService, private notificationService: NotificationService) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, ]]
    });

    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.email]],
      firstName: ['', [Validators.required, Validators.maxLength(32)]],
      lastName: ['', [Validators.required, Validators.maxLength(32)]],
      password: ['', [Validators.required]]
    });
  }
  ngOnInit(): void {

  }
  isRegistering: boolean = false;

  email: string = '';
  password: string = '';
  username: string = '';
  firstName: string = '';
  lastName: string = '';

  toggleForm() {
    this.isRegistering = !this.isRegistering;
  }

  onLogin() {
    if (this.loginForm.invalid) {
      return;
    }

    const { username, password } = this.loginForm.value;

    this.apiService.postData(API_ENDPOINTS.USER_LOGIN, { email:username, password }).subscribe(
      response => {
        this.apiService.setToken(response.token);
        this.router.navigate(['/record'], { state: { successMessage: 'Login Successful!' } });
        this.notificationService.showSuccess("Login successful!")
      },
      error => {
        this.notificationService.showError("Login failed!")
      }
    );
  }

  onRegister() {
    if (this.registerForm.invalid) {
      return;
    }
    const { username, password, firstName, lastName } = this.registerForm.value;
    this.apiService.postData(API_ENDPOINTS.USER_REGISTRATION, { email:username, password,  firstname:firstName, lastname: lastName }).subscribe(
      response => {
        this.toggleForm();
        this.notificationService.showSuccess("Registration successful!")
        this.notificationService.showSuccess(`Welcome ${firstName} ${lastName}`);
      },
      error => {
        this.notificationService.showError("Registration failed!")
      }
    );
  }


  hasError(form: FormGroup, controlName: string, errorName: string) {
    const control = form.get(controlName);
    return control && control.hasError(errorName) && control.touched;
  }
}
