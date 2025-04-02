import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { AuthentificationService } from "./authentification.service";
import { Observable } from "rxjs";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private authService: AuthentificationService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();
    console.log('Token récupéré pour la requête : ', token);
    console.log('URL de la requête : ', req.url);
    if (token && token.split('.').length === 3) { // ensure token format is valid
      console.log('Token valide, ajout du header Authorization');
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }else {
      console.log('Aucun token valide, envoi sans Authorization');
    }
    return next.handle(req);
  }
}
