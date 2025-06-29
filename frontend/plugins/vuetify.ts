import { createVuetify } from 'vuetify';
import { defineNuxtPlugin } from '#app';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';
import { VBtn, VCard, VTextField } from 'vuetify/components';
import 'vuetify/styles';
import '@mdi/font/css/materialdesignicons.css';

export default defineNuxtPlugin((nuxtApp) => {
  const vuetify = createVuetify({
    components: {
      VBtn,
      VCard,
      VTextField,
      ...components,
    },
    directives,
    theme: {
      defaultTheme: 'light',
    },
  });

  nuxtApp.vueApp.use(vuetify);
});