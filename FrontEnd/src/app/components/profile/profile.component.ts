import { Component } from '@angular/core';
import { User } from 'src/app/model/user.model';
import { AuthentificationService } from 'src/app/services/authentification.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {

  user: User | null = null;

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
}
