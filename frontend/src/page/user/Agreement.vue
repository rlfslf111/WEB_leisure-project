<template>
<div class="container col-sm-12 col-md-12 col-lg-12 p-0">
  <!-- background image -->
  <div class="agreement-img" style="display:block;">
  </div>
  <div class="container p-5">
    <div class="justify-content-center p-5">
      <span class="d-flex m-5" style="font-size:1.5rem; font-weight:bold" >이용약관</span>
      <small v-if="this.checkType == 'normal'" class="d-flex m-5" style="font-weight:bold">일반 사용자 전용</small>
      <small v-if="this.checkType == 'business' || this.checkType == 'admin'" class="d-flex m-5" style="font-weight:bold">사업자 전용</small>
      <div class="m-5">
        <AgreeNormal v-if="this.checkType == 'normal'"/>
        <AgreeBusiness v-if="this.checkType == 'business' || this.checkType == 'admin'"/>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import '../../assets/css/agreement.css'

import axios from 'axios'
const baseURL = process.env.VUE_APP_BACKURL;

import AgreeNormal from '../../components/agree/AgreeNormal.vue'
import AgreeBusiness from '../../components/agree/AgreeBusiness.vue'

export default {
  data: function() {
    return {
      checkType: 'normal',
    }
  },
  components: {
    AgreeNormal,
    AgreeBusiness,
  },
  methods: {
    authUser() {
      axios
        .get(`${baseURL}/account/authuser/${this.$cookies.get("Auth-Token")}`)
        .then((response) => {
          this.checkType = response.data.checkType;
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
  },
  created() {
    if (this.$cookies.get('Auth-Token') != null) {
      this.authUser()
    }
  },
}
</script>

<style>

</style>