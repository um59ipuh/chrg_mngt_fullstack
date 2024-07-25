import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateRecordDialogComponent } from './create-record-dialog.component';

describe('CreateRecordDialogComponent', () => {
  let component: CreateRecordDialogComponent;
  let fixture: ComponentFixture<CreateRecordDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreateRecordDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateRecordDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
