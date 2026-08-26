import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSanitario } from './admin-sanitario';

describe('AdminSanitario', () => {
  let component: AdminSanitario;
  let fixture: ComponentFixture<AdminSanitario>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminSanitario]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSanitario);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
