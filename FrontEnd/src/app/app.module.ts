import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { AccueilComponent } from './components/accueil/accueil.component';
import { ProfileComponent } from './components/profile/profile.component';
import { PublicationModalComponent } from './components/publication-modal/publication-modal.component';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FileUrlPipe } from './components/file-url.pipe';
import { ReactiveFormsModule } from '@angular/forms';
import { UpdateProfileComponent } from './components/update-profile/update-profile.component';
import { JwtInterceptor } from './services/jwt.interceptor';
import { AuthGuard } from './guards/auth.guard';
import { AuthentificationService } from './services/authentification.service';
import { EmailConfirmationComponent } from './components/email-confirmation/email-confirmation.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { CategoriesComponent } from './components/categories/categories.component';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignUpComponent,
    AccueilComponent,
    ProfileComponent,
    PublicationModalComponent,
    FileUrlPipe,
    UpdateProfileComponent,
    EmailConfirmationComponent,
    ResetPasswordComponent,
    CategoriesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },  // Intercepteur
    AuthGuard,  // Garde de route
    AuthentificationService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
