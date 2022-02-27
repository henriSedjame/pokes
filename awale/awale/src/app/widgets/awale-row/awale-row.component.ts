import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-awale-row',
  templateUrl: './awale-row.component.html',
  styleUrls: ['./awale-row.component.css']
})
export class AwaleRowComponent implements OnInit {


  @Input()
  holes!: Uint8Array

  @Input()
  user!: boolean

  @Input()
  turn!: boolean

  @Output()
  onHoleClick: EventEmitter<number> = new EventEmitter<number>()

  constructor() { }

  ngOnInit(): void {
  }

  click(n: number) {
    this.onHoleClick.emit(n)
  }
}
