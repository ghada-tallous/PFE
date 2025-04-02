import { Categorie } from "./categorie.model";
import { Media } from "./media.model";
import { User } from "./user.model";

export interface Publication{
    id?:number;
    date?: Date;
    titre: string;
    contenu:string;
    lien?:string;
    localisation?: string;
    active: true ; 
    categorie?: Categorie;
    categorieId?: number;
    user?:User;
    medias?: Media[];
}