<template>
  <div
    class="modal fade"
    id="postpermit"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header border-0 pl-0">
          <h5
            class="modal-title w-100 text-center font-weight-bold position-absolute"
            id="exampleModalLabel">{{post.email}}님의 작성글</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">

        <div class="card mt-5 mb-3" style="max-width: 100%;">
          <div class="row no-gutters">
            <div class="col-md-5">
              <img
                :src="makeimgurl(post.imgurl)"
                v-if="post.imgurl"
                class="card-img"
                style="height: 16rem; width:100%; box-shadow:5px 5px 5px rgba(0,0,0,.15)"
                alt
              />
            </div>
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
                    >#{{ hash }}</small>
                  </div>
                  <div class="d-flex justify-content-start">
                    <!-- 업체 위치 -->
                    <p
                      class="card-text"
                      style="font-size: 1rem; color: rgb(168, 168, 168); text-align: left; text-overflow:ellipsis; overflow: hidden; white-space: nowrap;"
                    >[{{ post.location }}]</p>
                  </div>
                  <!-- 제목 -->
                  <p
                    class="card-text font-weight-bold"
                    style="font-size: 1.2rem; text-align: left;"
                  >[{{ post.activity }}]{{ post.title }}</p>
                  <!-- season, place check badge -->
                  <div
                    style="text-align: left; text-overflow:ellipsis; overflow: hidden; white-space: nowrap;"
                  >
                    <b-badge
                      v-if="this.springCheck == 1"
                      pill
                      variant
                      style="background-color: #F699CD;"
                    >Spring</b-badge>
                    <b-badge
                      v-if="this.summerCheck == 1"
                      pill
                      variant
                      style="background-color: #32a852;"
                    >Summer</b-badge>
                    <b-badge
                      v-if="this.autumnCheck == 1"
                      pill
                      variant
                      style="background-color: #CCA38D"
                    >Autumm</b-badge>
                    <b-badge
                      v-if="this.winterCheck == 1"
                      pill
                      variant
                      style="background-color: #D3D3D3"
                    >Winter</b-badge>
                    <b-badge
                      v-if="this.placeCheck == 'ground'"
                      pill
                      variant
                      style="background-color: #501B00"
                    >Ground</b-badge>
                    <b-badge
                      v-if="this.placeCheck == 'water'"
                      pill
                      variant
                      style="background-color: #003399"
                    >Water</b-badge>
                    <b-badge
                      v-if="this.placeCheck == 'sky'"
                      pill
                      variant
                      style="background-color: #8DCCE7"
                    >Sky</b-badge>
                  </div>
                  <!-- 사용 기간 -->
                  <div class="d-flex justify-content-between">
                    <p
                      class="card-text mt-3"
                      style="font-size: 1rem; text-overflow:ellipsis; overflow: hidden; white-space:nowrap;"
                    >유효기간 {{ post.sdate }}~{{ post.edate }}</p>
                  </div>
                  <!-- 이용 가격 -->
                  <div class="d-flex justify-content-end mt-3">
                    <p
                      class="card-text mt-2 mr-4 mb-0"
                      style="font-size: 1rem; color: rgb(168, 168, 168); text-decoration:line-through;
                  "
                    >\{{ post.price }}</p>
                    <p
                      class="card-text font-weight-bold mb-0"
                      style="font-size: 1.5rem; text-align: left; margin-bottom: 5px;
                  "
                    >{{ post.price * 0.95 }} 원</p>
                  </div>
                  <hr class="mt-0" />
                  <!-- like heart -->
                </div>
              </div>
            </div>
          </div>
        </div>
      <br />
        <!-- 상세 정보 -->
        <h4 class="d-flex mb-3" style="font-weight:bold">상세정보</h4>
        <p class="d-flex" style="font-align:left">{{ post.detail }}</p>
        <hr />
        <!-- 업체 정보 -->
        <h4 class="d-flex mb-3" style="font-weight:bold">업체정보</h4>
        <p class="d-flex">{{ post.companyInfo }}</p>
        <hr />
        <!-- 지도 -->
        <p class="d-flex" style="font-size:1.5rem; font-weight:bold;">위치</p>
        <div style="max-width: 100%; height:300px;"></div>
        <small class="d-flex mt-2" style="font-weight:bold;">{{ post.location }}</small>            
      </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
          <button type="button" class="btn btn-primary" data-dismiss="modal">승인</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

const baseURL = process.env.VUE_APP_BACKURL;

export default {
    props:{
        post:Object,
    },
    dada(){
        return{
            
        }
    },
    created(){
       
    },
    methods:{
         makeimgurl(imgurl) {
      var url = "../../../contents/"+imgurl;
      return url;
    },
    },
};
</script>

<style>
</style>