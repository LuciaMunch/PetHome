import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalFormularioAdopcion } from './modal-formulario-adopcion';

describe('ModalFormularioAdopcion', () => {
  let component: ModalFormularioAdopcion;
  let fixture: ComponentFixture<ModalFormularioAdopcion>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalFormularioAdopcion]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalFormularioAdopcion);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
