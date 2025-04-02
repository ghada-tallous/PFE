import { Component } from '@angular/core';
import { User } from 'src/app/model/user.model';
import { AuthentificationService } from 'src/app/services/authentification.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-update-profile',
  templateUrl: './update-profile.component.html',
  styleUrls: ['./update-profile.component.css']
})
export class UpdateProfileComponent {
user: User | null = null;
selectedFile: File | null = null;

  constructor(private authService:AuthentificationService,private userService: UserService) { }
    ngOnInit(): void {
      // Retrieve the user object from the navigation state
      //this.user = history.state.user;
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
    }

    onFileChange(event: Event) {
      const input = event.target as HTMLInputElement;
      if (input.files && input.files.length > 0) {
        this.selectedFile = input.files[0];
      }
    }

    onSubmit() {
      if (this.user) {
        if (this.selectedFile) {
          const formData = new FormData();
          formData.append('file', this.selectedFile, this.selectedFile.name);
          // Upload the file first, then update the user
          this.userService.uploadFile(formData).subscribe(
            (response) => {
              this.user!.photo = response.filePath; // Assuming the response contains the file path
              this.updateUser();
              console.log('File uploaded and user updated successfully');
            },
            (error) => {
              console.error('Error uploading file:', error);
            }
          );
        } else {
          this.updateUser();
        }
      }
    }

    updateUser() {
      if (this.user) {
        this.userService.updateUser(this.user).subscribe(
          (response) => {
            console.log('Profile updated successfully');
          },
          (error) => {
            console.error('Error updating profile:', error);
          }
        );
      }
    }
}
