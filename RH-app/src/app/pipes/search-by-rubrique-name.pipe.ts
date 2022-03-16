import { Pipe, PipeTransform } from '@angular/core';
import { AppModule } from '../app.module';
import { Rubrique } from '../rubrique';

@Pipe({
  name: 'searchByRubriqueName'
})
export class SearchByRubriqueNamePipe implements PipeTransform {

  transform(rubrique : Rubrique, searchText: string): unknown {
    return (this.searchTree(rubrique,searchText));

  }

   searchTree(element : Rubrique, matchingTitle : string) : Rubrique | null{
    if(element.name == matchingTitle){
         return element;
    }else if (element.children != null){
         var i;
         var result = null;
         for(i=0; result == null && i < element.children.length; i++){
              result = this.searchTree(element.children[i], matchingTitle);
         }
         console.log(result);
         return result;

    }
    return null;

}
}
