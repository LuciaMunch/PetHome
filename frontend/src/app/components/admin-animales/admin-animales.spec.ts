import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminAnimales } from './admin-animales';

describe('AdminAnimales', () => {
  let component: AdminAnimales;
  let fixture: ComponentFixture<AdminAnimales>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminAnimales]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminAnimales);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
