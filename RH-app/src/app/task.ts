export class Task {
    id:number;
    name : string;
    description : string;
    start_date: string;
    end_date:string;
    status: string;
    tag:string;
    constructor(id:number,name:string) {
        this.id=id;
        this.name=name;
        this.tag="bug";

    }
}