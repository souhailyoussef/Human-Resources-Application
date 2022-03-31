export interface Rubrique {
    id: number;
    name : string;
    value : number;
    children : any;
    parent ?: Node;
    depth : number;
    rootNode : boolean;
    leafNode : boolean;
    file_id : number;

}
