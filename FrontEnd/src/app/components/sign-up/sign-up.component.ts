import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators,ValidationErrors, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthentificationService, SignUpRequest } from 'src/app/services/authentification.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit{
  signupForm: FormGroup;
  errorMsg: string = '';

  constructor(
    private fb: FormBuilder, 
    private authService: AuthentificationService, 
    private router: Router
  ) {
    this.signupForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  ngOnInit(): void {}

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      return { 'passwordMismatch': true };
    }
    return null;
  }

  onSubmit(): void {
    if (this.signupForm.invalid) {
      return;
    }
    const request: SignUpRequest = this.signupForm.value;
    console.log('Envoi de la requête signup : ', request);
    this.authService.signUp(request).subscribe({
      next: (response) => {
        console.log('Réponse signup réussie : ', response);
        /* // handle successful signup, e.g., redirect to signin
        const userData = {
          email: request.email,
          firstName: request.firstName,
          lastName: request.lastName,
          password: request.password
        };
        //this.router.navigate(['/accueil'], { state: { user: userData } });
        this.router.navigate(['/update-profile']); */
        // Rediriger vers la page de confirmation avec email et mot de passe
        this.router.navigate(['/email-confirmation'], {
          state: {
            email: request.email,
            password: request.password
          }
        });
      },
      error: (err) => {
        console.error('Erreur lors du signup : ', err);
        this.errorMsg = 'Signup failed';
      }
    });
  }

}
