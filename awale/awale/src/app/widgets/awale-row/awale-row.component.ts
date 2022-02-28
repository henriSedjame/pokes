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

  @Input()
  changeCode!: string;

  @Input()
  gain!: boolean;

  constructor() { }

  ngOnInit(): void {
  }

  click(n: number) {
    this.onHoleClick.emit(n)
  }

  isChanging(index: number): boolean {
    return this.user ? (this.changeCode == `u${index}`) : (this.changeCode == `p${index}`);
  }
}
