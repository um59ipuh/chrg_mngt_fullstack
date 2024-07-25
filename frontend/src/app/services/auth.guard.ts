import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from './api/api.service';
import {UI_ENDPOINTS} from "../constants/ui-endpoints";

export const authGuard: CanActivateFn = (route, state) => {
  const apiService = inject(ApiService);
  const router = inject(Router);

  if (apiService.isAuthenticated()) {
    return true;
  } else {
    if (apiService.isBackendRunning) {
      router.navigate([`/${UI_ENDPOINTS.AUTH}`]);
      return false;
    } else {
      return true;
    }
  }
};
