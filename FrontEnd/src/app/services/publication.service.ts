import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Publication } from '../model/publication.model';


const httpOptions = {
  headers: new HttpHeaders( {'Content-Type': 'application/json'} )
};
@Injectable({
  providedIn: 'root'
})
export class PublicationService {

  apiURL: string = 'http://localhost:8080/api/v1/user/publications';
  constructor(private http : HttpClient) { }

  createPublicationWithMedia(publicationData: any, files: File[]): Observable<Publication> {
    const formData: FormData = new FormData();
    formData.append('titre', publicationData.titre);
    formData.append('contenu', publicationData.contenu);
    if (publicationData.lien) {
      formData.append('lien', publicationData.lien);
    }
    if (publicationData.localisation) {
      formData.append('localisation', publicationData.localisation);
    }
    formData.append('active', String(publicationData.active));
    formData.append('categorieId', String(publicationData.categorieId));
    if (files && files.length) {
      Array.from(files).forEach((file: File) => {
        formData.append('files', file, file.name);
      });
    }
    return this.http.post<any>(`${this.apiURL}/with-media`, formData);
  }

  getAllPublications(): Observable<Publication[]> {
    return this.http.get<Publication[]>(this.apiURL);
  }

  updatePublication(id: number, publication: any, files: File[]): Observable<Publication> {
    const formData = new FormData();
    formData.append('titre', publication.titre);
    formData.append('contenu', publication.contenu);
    if (publication.lien) formData.append('lien', publication.lien);
    if (publication.localisation) formData.append('localisation', publication.localisation);
    formData.append('active', publication.active.toString());
    formData.append('categorieId', publication.categorieId?.toString() || '');
    files.forEach(file => formData.append('files', file));
    return this.http.put<Publication>(`${this.apiURL}/${id}`, formData);
  }

  deletePublication(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`);
  }
 

  
}
