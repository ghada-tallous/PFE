import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthentificationService, JwtAuthentificationResponse, SignInRequest } from 'src/app/services/authentification.service';
import { Router } from '@angular/router';

declare var bootstrap: any; // Pour utiliser Bootstrap JS

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  signinForm: FormGroup;
  forgotPasswordForm: FormGroup;
  errorMsg: string = '';
  forgotPasswordMessage: string = '';
  
  constructor(private fb: FormBuilder, private authService: AuthentificationService,private router: Router) {
    this.signinForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
    this.forgotPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.signinForm.invalid) {
      return;
    }
    const request: SignInRequest = this.signinForm.value;
    this.authService.signIn(request).subscribe({
      next: (response: JwtAuthentificationResponse) => {
        // handle the token response, e.g., store in local storage
        localStorage.setItem('token', response.token);
        localStorage.setItem('refreshToken', response.refreshToken);
        //this.router.navigate(['/accueil'], { state: { user: { email: request.email } } });
        this.router.navigate(['/accueil']);
      },
      error: (err) => {
        this.errorMsg = 'Signin failed';
      } 
    });
  }

  openForgotPasswordModal(): void {
    const modalElement = document.getElementById('forgotPasswordModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  closeForgotPasswordModal(): void {
    const modalElement = document.getElementById('forgotPasswordModal');
    if (modalElement) {
      const modal = bootstrap.Modal.getInstance(modalElement);
      modal.hide();
    }
  }

  resetForgotPasswordModal(): void {
    this.forgotPasswordForm.reset(); // Réinitialise le formulaire
    this.forgotPasswordMessage = ''; // Efface le message
  }

  onForgotPasswordSubmit(): void {
    if (this.forgotPasswordForm.invalid) {
      return;
    }
    const request = this.forgotPasswordForm.value;
    this.authService.forgotPassword(request).subscribe({
      next: (response) => {
        this.forgotPasswordMessage = response; // "Un lien de réinitialisation a été envoyé à votre email."
        setTimeout(() => {
          this.closeForgotPasswordModal(); // Ferme la modale
          this.resetForgotPasswordModal(); // Réinitialise après fermeture
        }, 2000); // Délai de 2 secondes pour que l'utilisateur voie le message
      },
      error: (err) => {
        this.forgotPasswordMessage = 'Erreur : ' + (err.error || 'Impossible d\'envoyer le lien.');
      }
    });
  }

}
