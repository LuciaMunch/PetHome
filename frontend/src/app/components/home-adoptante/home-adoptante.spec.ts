import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeAdoptante } from './home-adoptante';

describe('HomeAdoptante', () => {
  let component: HomeAdoptante;
  let fixture: ComponentFixture<HomeAdoptante>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeAdoptante]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeAdoptante);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
