import { Component, OnInit } from '@angular/core';
import { Wizard } from 'app/Interfaces/Wizard';
import { WizardService } from 'app/services/wizard.service';
import { World } from 'app/Interfaces/World';
import { WorldService } from 'app/services/world.service';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {

  protected wizard: Wizard;

  constructor(private wizardService : WizardService, private worldService : WorldService) { }

  ngOnInit() {
    //this.init();
  }

  private init(): any {
    /*this.wizardService.getWizardMessage(this.getWorld()).subscribe(wizard => {
      this.wizard = wizard;
    });*/
  } 

  private getWorld(): World {
    return this.worldService.getCurrentWorld();
  }

}
