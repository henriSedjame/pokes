import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AwaleRowComponent } from './awale-row.component';

describe('AwaleRowComponent', () => {
  let component: AwaleRowComponent;
  let fixture: ComponentFixture<AwaleRowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AwaleRowComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AwaleRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
