import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DialogoDetailComponent } from './dialogo-detail.component';

describe('Dialogo Management Detail Component', () => {
  let comp: DialogoDetailComponent;
  let fixture: ComponentFixture<DialogoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DialogoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dialogo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DialogoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DialogoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dialogo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dialogo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
