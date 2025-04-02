import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { AccueilComponent } from './components/accueil/accueil.component';
import { ProfileComponent } from './components/profile/profile.component';
import { PublicationModalComponent } from './components/publication-modal/publication-modal.component';
import { AuthGuard } from './guards/auth.guard';
import { UpdateProfileComponent } from './components/update-profile/update-profile.component';
import { EmailConfirmationComponent } from './components/email-confirmation/email-confirmation.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { CategoriesComponent } from './components/categories/categories.component';

const routes: Routes = [
  {path :'login',component : LoginComponent},
  {path :'sign-up',component : SignUpComponent},
  {path : 'email-confirmation', component: EmailConfirmationComponent },
  {path : 'reset-password', component: ResetPasswordComponent },
  {path :'accueil',component : AccueilComponent,canActivate: [AuthGuard]},
  {path :'categories',component : CategoriesComponent,canActivate: [AuthGuard]},
  {path :'profile',component : ProfileComponent,canActivate: [AuthGuard]},
  {path :'update-profile',component : UpdateProfileComponent,canActivate: [AuthGuard]},
  {path :'publicationModal',component : PublicationModalComponent,canActivate: [AuthGuard]},
  { path : '', redirectTo : '/login', pathMatch : 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
