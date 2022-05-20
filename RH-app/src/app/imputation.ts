import { Task } from "./task";
export class Imputation {
    id?:number;
    task : Task;
    day : string;
    workload: number;
    //status:string;
    comment: string;

    constructor(task : Task,day : string, workload : number) {
        this.task=task;
        this.day = day;
        this.workload=workload;
        this.comment=""
    }
}