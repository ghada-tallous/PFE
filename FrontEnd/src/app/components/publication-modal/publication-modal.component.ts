import { Component, EventEmitter,Input, Output, SimpleChanges } from '@angular/core';
import { Publication } from '../../model/publication.model';
import { HttpClient } from '@angular/common/http';
import { PublicationService } from 'src/app/services/publication.service';
import { NgForm } from '@angular/forms';
import { Categorie } from 'src/app/model/categorie.model';
import { CategorieService } from 'src/app/services/categorie.service';

@Component({
  selector: 'app-publication-modal',
  templateUrl: './publication-modal.component.html',
  styleUrls: ['./publication-modal.component.css']
})
export class PublicationModalComponent {
  
  @Input() isEditMode: boolean = false; // Mode édition ou création
  @Input() publicationToEdit: Publication | null = null; // Publication à modifier
  @Output() publicationAjoutee = new EventEmitter<Publication>(); // Pour création
  @Output() publicationUpdated = new EventEmitter<Publication>(); // Pour mise à jour

  publication: Publication = {
    titre: '',
    contenu: '',
    lien: undefined,
    localisation: undefined,
    active: true,
    categorieId: undefined,
  };
  selectedFiles: File[] = [];
  categories: Categorie[] = [];
  
  // Variable pour contrôler l'affichage de la zone de saisie pour le lien
  isLinkVisible: boolean = false;
  isLocationVisible: boolean = false;
  uploadedMedia: string[] = [];
  
  constructor(private publicationService: PublicationService, private categorieService: CategorieService) {}
  
  ngOnInit(): void {
    this.categorieService.getAllCategories().subscribe(
      (data) => {
        this.categories = data;
        // Si en mode édition, s'assurer que la catégorie est bien sélectionnée après le chargement
      if (this.isEditMode && this.publicationToEdit && this.publicationToEdit.categorieId) {
        this.publication.categorieId = this.publicationToEdit.categorieId;
      }
      },
      (error) => {
        console.error('Error fetching categories:', error);
      }
    );
  }

      // Si en mode édition, pré-remplir le formulaire
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['publicationToEdit'] && this.isEditMode && this.publicationToEdit) {
      console.log('Publication à modifier :', this.publicationToEdit); // Debug
      // Mettre à jour this.publication avec les données de publicationToEdit
      this.publication = { ...this.publicationToEdit };
      this.publication.categorieId= this.publicationToEdit.categorie?.id; // Assurez-vous que la catégorie est bien définie
      console.log('categorieId dans publication :', this.publication.categorieId);
      this.isLinkVisible = !!this.publication.lien; // Afficher le champ lien si existant
      this.isLocationVisible = !!this.publication.localisation; // Afficher le champ localisation si existant
      console.log('Publication updated in edit mode:', this.publication); // Pour déboguer
    }
  }

  // Handling file selection
  onFileChange(event: Event, form: NgForm) {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.selectedFiles = Array.from(input.files);
      const label = document.getElementById('mediaLabel');
      if (label) {
        label.textContent ="Photos/vidéos";
      }
      // Afficher la liste des fichiers sélectionnés dans la console
      this.selectedFiles.forEach(file => {
        console.log(`File: ${file.name}, Size: ${file.size}, Type: ${file.type}`);
      });
      // Soumettre le formulaire automatiquement
      form.ngSubmit.emit();
    }
  }

      onSubmit(): void {
        if (this.isEditMode && this.publicationToEdit?.id) {
          console.log('ID de la publication à modifier :', this.publicationToEdit.id); // Debug
          console.log('Publication envoyée :', this.publication); // Debug
          // Mode édition
          this.publicationService.updatePublication(this.publicationToEdit.id, this.publication, this.selectedFiles).subscribe({
            next: (response) => {
              console.log('Publication updated:', response);
              this.publicationUpdated.emit(response);// Émet la publication mise à jour
              this.resetForm();
              this.closeModal();
            },
            error: (error) => console.error('Error updating publication:', error)
          });
        } else {
          // Mode création
        this.publicationService.createPublicationWithMedia(this.publication, this.selectedFiles)
          .subscribe({
            next: response => {
              console.log('Publication created:', response);
              this.publicationAjoutee.emit(response); // Émet la nouvelle publication au parent
              this.resetForm(); // Réinitialise le formulaire
              this.closeModal();
            },
            error: error => console.error('Error creating publication:', error)
          });
        }
      }
      resetForm(): void {
        this.publication = {
          titre: '',
          contenu: '',
          lien: '',
          localisation: '',
          active: true,
          categorieId: undefined,
        };
        this.selectedFiles = [];
        this.isLinkVisible = false;
        this.isLocationVisible = false;
      }

      closeModal(): void {
        const modal = document.getElementById('publicationModal');
        if (modal) {
          modal.classList.remove('show');
          modal.style.display = 'none';
          modal.removeAttribute('aria-modal');
          modal.removeAttribute('role');
        }
        this.resetForm();
      }
    
  
 // Cette méthode sera appelée lorsque l'on clique sur l'option "Lien"
 toggleLinkInput() {
  this.isLinkVisible = !this.isLinkVisible; // Alterne l'affichage
}
// Méthode pour basculer l'affichage du champ de localisation
toggleLocationInput() {
  this.isLocationVisible = !this.isLocationVisible;
}
onSelectCategorie(event: any) {
  this.publication.categorieId = event.target.value;
}

  triggerFileInput() {
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.click();
    }
  }
  
   /*  onFileSelected(event: any): void {
      this.selectedFiles = event.target.files;
    } */

      onFileSelected(event: any): void {
        this.selectedFiles = Array.from(event.target.files);
      }
  /* isImage(file: File): boolean {
    return file.type.endsWith('.jpg') || file.type.endsWith('.jpeg') || file.type.endsWith('.png');
  }

  isVideo(file: File): boolean {
    return file.type.endsWith('.mp4') || file.type.endsWith('.avi') || file.type.endsWith('.mov');
  } */
}
