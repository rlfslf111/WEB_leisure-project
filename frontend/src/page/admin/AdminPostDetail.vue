<template>
  <div class="container col-sm-12 col-md-12 col-lg-12 p-0">
    <!-- background image -->
    <div class="admindetail-img" style="display:block;">
      <div class="info-bg"></div>
    </div>
    <div class="container col-md-10" style="margin-top: 100px">
      <div class="column">
        <div class="card mt-5 mb-3" style="max-width: 100%;">
          <div class="row no-gutters">
            <div class="col-md-4">
              <img
                :src="makeimgurl(post.imgurl)"
                v-if="post.imgurl"
                class="card-img"
                style="height: 16rem; box-shadow:5px 5px 5px rgba(0,0,0,.15)"
                alt
              />
            </div>
            <div class="col-md-1"></div>
            <div class="col-md-7">
              <div class="card-body" style="padding: 0 0 0 20px">
                <div class="text">
                  <div class="d-flex justify-content-start">
                    <!-- hashTag -->
                    <small
                      class="text-primary mr-1"
                      style="text-align:left; font-size:1rem; text-overflow:ellipsis; overflow: hidden; white-space: nowrap;"
                      v-for="hash in hashTag"
                      :key="hash.id"
                      >#{{ hash }}</small
                    >
                    <div class="ml-auto"></div>
                  </div>
                  <div class="d-flex justify-content-start">
                    <!-- 업체 위치 -->
                    <p
                      class="card-text"
                      style="font-size: 1rem; color: rgb(168, 168, 168); text-align: left; text-overflow:ellipsis; overflow: hidden; white-space: nowrap;"
                    >
                      [{{ post.location }}]
                    </p>
                  </div>
                  <!-- 제목 -->
                  <p class="card-text font-weight-bold" style="font-size: 1.2rem; text-align: left;">
                    [{{ post.activity }}]{{ post.title }}
                  </p>
                  <!-- season, place check badge -->
                  <div style="text-align: left; text-overflow:ellipsis; overflow: hidden; white-space: nowrap;">
                    <b-badge v-if="this.springCheck == 1" pill variant style="background-color: #F699CD;">Spring</b-badge>
                    <b-badge v-if="this.summerCheck == 1" pill variant style="background-color: #32a852;">Summer</b-badge>
                    <b-badge v-if="this.autumnCheck == 1" pill variant style="background-color: #CCA38D">Autumm</b-badge>
                    <b-badge v-if="this.winterCheck == 1" pill variant style="background-color: #D3D3D3">Winter</b-badge>
                    <b-badge v-if="this.placeCheck == 'ground'" pill variant style="background-color: #501B00">Ground</b-badge>
                    <b-badge v-if="this.placeCheck == 'water'" pill variant style="background-color: #003399">Water</b-badge>
                    <b-badge v-if="this.placeCheck == 'sky'" pill variant style="background-color: #8DCCE7">Sky</b-badge>
                  </div>
                  <!-- 사용 기간 -->
                  <div class="d-flex justify-content-between">
                    <p class="card-text mt-3" style="font-size: 1rem; text-overflow:ellipsis; overflow: hidden; white-space:nowrap;">
                      유효기간 {{ post.sdate }}~{{ post.edate }}
                    </p>
                    <!-- 신고하기 버튼 -->
                  </div>
                  <!-- 이용 가격 -->
                  <div class="d-flex justify-content-end" style="margin-top:4rem;">
                    <p class="card-text font-weight-bold mb-0" style="font-size: 1.5rem; text-align: left; margin-bottom: 5px;">
                      {{ post.price }} 원
                    </p>
                  </div>
                  <hr class="mt-0" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Scrollspy  -->
      <nav id="navbar-example2" class="navbar nav-info" style="position: sticky; top: 0; z-index:100;">
        <ul class="nav justify-content-between" style="width:70%; margin:auto">
          <li class="nav-item">
            <a class="nav-link info-link" href="#item" @click="scroll" style="font-size:0.9rem; width:100%;">상세정보</a>
          </li>
          <li class="nav-item">
            <a class="nav-link info-link" href="#corp" @click="scroll" style="font-size:0.9rem; width:100%">업체정보</a>
          </li>
        </ul>
      </nav>
      <br />
      <div data-spy="scroll" data-target="#navbar-example2" data-offset="0">
        <!-- 상세 정봉 -->
        <h4 id="item" class="d-flex mb-3" style="font-weight:bold">상세정보</h4>
        <Viewer v-if="post.detail != null" :initialValue="post.detail" style="text-align:justify;"/>
        <hr />
        <!-- 업체 정보 -->
        <h4 id="corp" class="d-flex mb-3" style="font-weight:bold">업체정보</h4>
        <p class="d-flex">{{ post.companyInfo }}</p>
        <hr />
        <!-- 지도 -->
        <p class="d-flex" style="font-size:1.5rem; font-weight:bold;">위치</p>
        <div id="map" style="max-width: 100%; height:300px;"></div>
        <small class="d-flex mt-2" style="font-weight:bold;">{{ post.location }}</small>
        <hr class="mt-2" />
      </div>
      <!-- 거절 승인 -->
      <div class="d-flex justify-content-end mb-5">
        <button type="button" class="btn btn-danger mr-1" @click="postreject(post.pid)" style="font-weight:bold;"><i class="fas fa-times mr-2"></i>거절</button>
        <button class="btn btn-default" @click="postpermit(post.pid)" style="background-color:#86a5d4; color:white; font-weight:bold;"><i class="far fa-hand-point-up mr-2"></i>승인</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

import 'codemirror/lib/codemirror.css';
import '@toast-ui/editor/dist/toastui-editor.css';
import { Viewer } from '@toast-ui/vue-editor';

const baseURL = process.env.VUE_APP_BACKURL;

export default {
  components: {
    Viewer,
  },
  data() {
    return {
      post: [],
      pid: '',
      email: '',
      hashTag: [],
      checkType: '',
      springCheck: '',
      summerCheck: '',
      autumnCheck: '',
      winterCheck: '',
      placeCheck: '',
    };
  },
  created() {
    this.pid = this.$route.params.ID;
    this.getPost();
    this.fetchHashTag();
  },
  methods: {
    scroll(evt) {
      evt.preventDefault();
      const href = evt.target.getAttribute('href');
      var location = document.querySelector(href).offsetTop;
      window.scrollTo({ top: location + 360, behavior: 'smooth' });
    },
    makeimgurl(imgurl) {
      var url = "../../../contents/"+imgurl;
      return url;
    },
    getPost() {
      axios
        .get(`${baseURL}/post/detail/${this.$route.params.ID}`)
        .then((res) => {
          this.springCheck = res.data.spring;
          this.summerCheck = res.data.summer;
          this.autumnCheck = res.data.autumn;
          this.winterCheck = res.data.winter;
          this.placeCheck = res.data.place;
          this.post = res.data;
          this.mapView(this.post.location);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    mapView(loc) {
      var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
          center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
          level: 5, // 지도의 확대 레벨
        };

      // 지도를 생성합니다
      var map = new kakao.maps.Map(mapContainer, mapOption);

      // 주소-좌표 변환 객체를 생성합니다
      var geocoder = new kakao.maps.services.Geocoder();

      // 주소로 좌표를 검색합니다
      geocoder.addressSearch(loc, function(result, status) {
        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {
          var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

          // 결과값으로 받은 위치를 마커로 표시합니다
          var marker = new kakao.maps.Marker({
            map: map,
            position: coords,
          });
          var test = loc;

          // 인포윈도우로 장소에 대한 설명을 표시합니다
          var infowindow = new kakao.maps.InfoWindow({
            content: test,
          });
          infowindow.open(map, marker);

          // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
          map.setCenter(coords);
        }
      });
    },
    fetchHashTag() {
      axios
        .get(`${baseURL}/tag/list/${this.pid}`)
        .then((response) => {
          this.hashTag = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
    },

    // 승인
    postpermit(pid) {
      Swal.fire({
        width: 350,
        text: '승인하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#fff',
        cancelButtonColor: '#fff',
        confirmButtonText: '<a style="font-size:1rem; color:black;">승인</a>',
        cancelButtonText: '<a style="font-size:1rem; color:black;">취소</a>',
      }).then((result) => {
        if (result.value) {
          const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1000,
            timerProgressBar: true,
            onOpen: (toast) => {
              toast.addEventListener('mouseenter', Swal.stopTimer);
              toast.addEventListener('mouseleave', Swal.resumeTimer);
            },
          });
          Toast.fire({
            icon: 'success',
            title: '포스트가 승인되었습니다.',
          });
          axios
            .get(`${baseURL}/post/permit/${pid}`)
            .then((res) => {
              this.$router.push('/admin');
            })
            .catch((err) => {
              console.log(err);
            });
        }
      });
    },

    // 거절
    postreject(pid) {
      Swal.fire({
        width: 350,
        text: '거절하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#fff',
        cancelButtonColor: '#fff',
        confirmButtonText: '<a style="font-size:1rem; color:black;">거절</a>',
        cancelButtonText: '<a style="font-size:1rem; color:black;">취소</a>',
      }).then((result) => {
        if (result.value) {
          const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1000,
            timerProgressBar: true,
            onOpen: (toast) => {
              toast.addEventListener('mouseenter', Swal.stopTimer);
              toast.addEventListener('mouseleave', Swal.resumeTimer);
            },
          });
          Toast.fire({
            icon: 'success',
            title: '포스트가 거절되었습니다.',
          });
          axios
            .delete(`${baseURL}/post/delete/${pid}`)
            .then(() => {
              this.$router.push('/admin');
            })
            .catch((err) => {
              console.log(err);
            });
        }
      });
    },
  },
};
</script>

<style></style>
