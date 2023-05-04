import { Component } from '@angular/core';
import { webSocket } from 'rxjs/webSocket';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  private webSocketSubject$;

  constructor() {
    this.webSocketSubject$ = webSocket('ws://localhost:9090/secured-websocket');

    this.webSocketSubject$.asObservable()
      .subscribe({
        next: (v) => console.log(v),
        error: (err) => console.log(err),
        complete: () => console.log("Complete")
      });
  }
}
