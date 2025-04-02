import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Publication } from 'src/app/model/publication.model';
import { User } from 'src/app/model/user.model';
import { AuthentificationService } from 'src/app/services/authentification.service';
import { PublicationService } from 'src/app/services/publication.service';
import { UserService } from 'src/app/services/user.service';
declare var $: any; // Declare jQuery for TypeScript

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent {
user: User | null = null;
  publications: Publication[] = [];

  backendUrl = 'http://localhost:8080';// URL de base pour les médias
  selectedPublication: Publication | null = null; // Publication à modifier
  isEditMode: boolean = false; // Indicateur pour le mode édition
  publicationToDelete: Publication | null = null;
  
  constructor(
    private authService: AuthentificationService,
    private userService: UserService,
    private publicationService: PublicationService) { }

  ngAfterViewInit() {
    // Initialize Bootstrap dropdowns after the view is rendered
    $(document).ready(() => {
      $('.dropdown-toggle').dropdown();
    });
  }
  ngOnInit(): void {

    const email = this.authService.getEmail();

    if (email) {
      this.authService.getUser(email).subscribe(
        (user) => {
          this.user = user;
        },
        (error) => {
          console.error('Error fetching user:', error);
        }
      );
    }
    this.getAllPublications();
  }
  isPublicationModalVisible: boolean = false;

  openPublicationModal() {
    this.isEditMode = false;
    this.selectedPublication = null;
    //this.isPublicationModalVisible = true;
    const modal = document.getElementById('publicationModal');
    if (modal) {
      modal.classList.add('show');
      modal.style.display = 'block';
      modal.setAttribute('aria-modal', 'true');
      modal.setAttribute('role', 'dialog');
    }
  }

  openEditPublicationModal(publication: Publication) {
    this.isEditMode = true;
    this.selectedPublication = { ...publication };
    const modal = document.getElementById('publicationModal');
    if (modal) {
      modal.classList.add('show');
      modal.style.display = 'block';
      modal.setAttribute('aria-modal', 'true');
      modal.setAttribute('role', 'dialog');
    }
  }
  closePublicationModal() {
    this.isEditMode = false;
    this.selectedPublication = null;
    //this.isPublicationModalVisible = false;
    const modal = document.getElementById('publicationModal');
    if (modal) {
      modal.classList.remove('show');
      modal.style.display = 'none';
      modal.removeAttribute('aria-modal');
      modal.removeAttribute('role');
    }
  }

  logOut() {
    this.authService.logout();
  }

  getAllPublications() {
    this.publicationService.getAllPublications().subscribe({
      next: data => this.publications = data,
      error: err => console.error('Error:', err)
    });
  }

  getMediaUrl(relativeUrl: string): string {
    // Replace backslashes with forward slashes if necessary
    const fixedUrl = relativeUrl.replace(/\\/g, '/');
    return `${this.backendUrl}/${fixedUrl}`;
  }

  // Méthode pour ajouter la nouvelle publication en tête
  onPublicationCreated(newPublication: Publication) {
    //this.publications.unshift(newPublication); // Ajoute en tête de la liste
    this.getAllPublications(); // Recharge la liste
    this.closePublicationModal(); // Ferme la modale
  }
  onPublicationUpdated(updatedPublication: Publication) {
    const index = this.publications.findIndex(p => p.id === updatedPublication.id);
    if (index !== -1) {
      this.publications[index] = updatedPublication;
    }
    this.closePublicationModal();
  }

  // Ouvrir la modale de confirmation de suppression
  openDeleteModal(publication: Publication) {
    this.publicationToDelete = publication;
    const modal = document.getElementById('deleteConfirmationModal');
    if (modal) {
      modal.classList.add('show');
      modal.style.display = 'block';
      modal.setAttribute('aria-modal', 'true');
      modal.setAttribute('role', 'dialog');
    }
  }

  // Fermer la modale de confirmation
  closeDeleteModal() {
    this.publicationToDelete = null;
    const modal = document.getElementById('deleteConfirmationModal');
    if (modal) {
      modal.classList.remove('show');
      modal.style.display = 'none';
      modal.removeAttribute('aria-modal');
      modal.removeAttribute('role');
    }
  }
  // Confirmer la suppression
  confirmDelete() {
    if (this.publicationToDelete && this.publicationToDelete.id) {
      this.publicationService.deletePublication(this.publicationToDelete.id).subscribe({
        next: () => {
          this.publications = this.publications.filter(p => p.id !== this.publicationToDelete!.id);
          console.log('Publication supprimée avec succès');
          this.closeDeleteModal();
        },
        error: (error) => {
          console.error('Erreur lors de la suppression de la publication:', error);
          alert('Une erreur est survenue lors de la suppression.');
          this.closeDeleteModal();
        }
      });
    }
  }

  /* deletePublication(publication: Publication) {
    const confirmation = confirm('Voulez-vous vraiment supprimer cette publication ?');
    if (confirmation && publication.id) {
      this.publicationService.deletePublication(publication.id).subscribe({
        next: () => {
          // Supprimer la publication de la liste localement
          this.publications = this.publications.filter(p => p.id !== publication.id);
          console.log('Publication supprimée avec succès');
        },
        error: (error) => {
          console.error('Erreur lors de la suppression de la publication:', error);
          alert('Une erreur est survenue lors de la suppression.');
        }
      });
    }
  } */

}
