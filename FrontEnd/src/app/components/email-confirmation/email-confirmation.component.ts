import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthentificationService } from 'src/app/services/authentification.service';

@Component({
  selector: 'app-email-confirmation',
  templateUrl: './email-confirmation.component.html',
  styleUrls: ['./email-confirmation.component.css']
})
export class EmailConfirmationComponent implements OnInit {
  email: string = '';
  token: string | null = null;
  confirmationStatus: string = '';
  isChecking: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthentificationService
  ) {}

  ngOnInit(): void {
    // Récupérer l'email et le token depuis les paramètres ou l'état
    this.email = history.state.email || '';
    this.token = this.route.snapshot.queryParamMap.get('token');

    // Si un token est présent dans l'URL, vérifier immédiatement
    if (this.token) {
      this.confirmEmail();
    }
  }

  confirmEmail(): void {
    if (!this.token) {
      this.confirmationStatus = 'Aucun token de confirmation trouvé.';
      return;
    }

    this.isChecking = true;
    this.authService.confirmEmail(this.token).subscribe({
      next: (response) => {
        this.confirmationStatus = response; // "Email confirmed successfully..."
        this.isChecking = false;
      },
      error: (err) => {
        this.confirmationStatus = err.error || 'Erreur lors de la confirmation.';
        this.isChecking = false;
      }
    });
  }

  checkConfirmation(): void {
    if (!this.email || !history.state.password) {
      this.confirmationStatus = 'Informations de connexion manquantes.';
      return;
    }

    this.isChecking = true;
    const signInRequest = { email: this.email, password: history.state.password };
    this.authService.signIn(signInRequest).subscribe({
      next: () => {
        this.router.navigate(['/accueil']);
      },
      error: (err) => {
        this.confirmationStatus = 'Veuillez confirmer votre email avant de vous connecter.';
        this.isChecking = false;
      }
    });
  }
}