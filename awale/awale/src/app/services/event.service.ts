import { Injectable } from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EventService {

  public pcChoiceEmitter: Subject<number> = new Subject();

  public endGameEmitter: Subject<boolean> = new Subject<boolean>();

  public updateGameEmitter: Subject<boolean> = new Subject<boolean>();

  constructor() { }
}
