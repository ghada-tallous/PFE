import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ValidationErrors, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthentificationService } from 'src/app/services/authentification.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  resetForm: FormGroup;
  resetMessage: string = '';
  token: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthentificationService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.resetForm = this.fb.group({
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token');
    if (!this.token) {
      this.resetMessage = 'Token manquant. Veuillez utiliser le lien envoyé par email.';
    }
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('newPassword');
    const confirmPassword = control.get('confirmPassword');
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      return { 'passwordMismatch': true };
    }
    return null;
  }

  onSubmit(): void {
    if (this.resetForm.invalid || !this.token) {
      return;
    }
    const request = { newPassword: this.resetForm.value.newPassword };
    this.authService.resetPassword(this.token, request).subscribe({
      next: (response) => {
        this.resetMessage = response; // "Password has been reset successfully."
        setTimeout(() => this.router.navigate(['/login']), 2000); // Redirection après 2s
        
      },
      error: (err) => {
        this.resetMessage = err.error || 'Erreur lors de la réinitialisation.';
      }
    });
  }
}