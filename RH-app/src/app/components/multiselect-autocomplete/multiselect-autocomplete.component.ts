import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatAutocomplete } from '@angular/material/autocomplete';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatOption } from '@angular/material/core';
import { Observable, startWith,map } from 'rxjs';
import { Project, Task, User } from '../timesheets/timesheets.component';


@Component({
  selector: 'app-multiselect-autocomplete',
  templateUrl: './multiselect-autocomplete.component.html',
  styleUrls: ['./multiselect-autocomplete.component.css']
})
export class MultiselectAutocompleteComponent implements OnInit {

  @ViewChild("projectInput") private _projectInput: ElementRef;
  projectControl = new FormControl();

  @Input('data') data : any;
  selectedProjects: Project[] = new Array<Project>();

  @Input('placeholder') text : string;

  filteredProjects: Observable<Project[]>;
  lastFilter: string = "";

  ngOnInit() {
    this.filteredProjects = this.projectControl.valueChanges.pipe(
      startWith<string | Project[]>(""),
      map(value => (typeof value === "string" ? value : this.lastFilter)),
      map(filter => this.filter(filter))
    );
  }

  filter(filter: string): Project[] {
    this.lastFilter = filter;
    if (filter) {
      return this.data.filter((option  : Project )=> {
        return (
          option.project_name.toLowerCase().indexOf(filter.toLowerCase()) >= 0 
        );
      });
    } else {
      return this.data.slice();
    }
  }

  displayFn(value: Project[] | string): string  {
    let displayValue: string= '';
    if (Array.isArray(value)) {
      value.forEach((project, index) => {
        if (index === 0) {
          displayValue = project.project_name + " ";

        } else {
          displayValue += ", " + project.project_name + " ";

        }

      });
    } else {
      displayValue = value;

    }
    return displayValue;

  }

  optionClicked(event: Event, project: Project) {
    event.stopPropagation();
    this.toggleSelection(project);
    
  }

  toggleSelection(project: Project) {
    project.selected = !project.selected;
    if (project.selected) {
      this.selectedProjects.push(project);
    } else {
      const i = this.selectedProjects.findIndex(
        value =>
          value.project_name === project.project_name 
      );
      this.selectedProjects.splice(i, 1);
    }

    // this.userControl.setValue(this.selectedUsers);
    // this.userControl.setValue("");
    this._projectInput.nativeElement.focus();
  }
}



