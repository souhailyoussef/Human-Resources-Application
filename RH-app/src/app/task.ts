export class Task {
    id:number;
    name : string;
    description ?: string;
    start_date?: string;
    end_date?:string;
    status?: string;
    tag?:string;
    project_id?: number
    constructor(id:number,name:string, project_id?: number) {
        this.id=id;
        this.name=name;
        this.tag="bug";
        this.project_id=project_id;
    }
   
}