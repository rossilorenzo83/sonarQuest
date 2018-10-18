import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Wizard} from '../Interfaces/Wizard';
import {World} from '../Interfaces/World';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class WizardService {

  constructor(private http: HttpClient) {
  }

  public getWizardMessage(world: World): Observable<Wizard> {
    if (world != null) {
      return this.http.get<Wizard>(`${environment.endpoint}/wizard/${world.id}`);
    }
  }


}
