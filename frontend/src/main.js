import Vue from 'vue';
import App from './App.vue';
import router from './router';
import VueCookies from 'vue-cookies';
import VueBootstrapToasts from 'vue-bootstrap-toasts';
import Toasted from 'vue-toasted';
import VueSimpleAlert from 'vue-simple-alert';

import BootstrapVue from 'bootstrap-vue';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';

Vue.use(VueSimpleAlert);

Vue.use(BootstrapVue);

Vue.use(Toasted);

Vue.use(VueBootstrapToasts);

Vue.use(VueCookies);
/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    components: { App },
    template: '<App/>',
});