import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptorsFromDi, HTTP_INTERCEPTORS } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { registerLocaleData } from '@angular/common';
import fr from '@angular/common/locales/fr';
import { NZ_I18N, fr_FR } from 'ng-zorro-antd/i18n';
import { NzMessageModule } from 'ng-zorro-antd/message';
import { FormsModule } from '@angular/forms';
import { LOCALE_ID } from '@angular/core';

import { routes } from './app.routes';
import { ErrorInterceptor } from './core/interceptors/error.interceptor';

registerLocaleData(fr);

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    provideAnimations(),
    importProvidersFrom(NzMessageModule, FormsModule),
    { provide: NZ_I18N, useValue: fr_FR },
    { provide: LOCALE_ID, useValue: 'fr-FR' },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ]
};
