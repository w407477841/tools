<template>
  <section>
  <Form @submit.native.prevent>
    <FormItem>
      <Input v-model="key" placeholder="输入地址" style="width:300px"><Button slot="append" icon="ios-search" @click="search"></Button></Input>
    </FormItem>
    <FormItem v-if="!this.isClose">
      <Button @click="getProjedtScope" type="primary">确定项目范围</Button>
    </FormItem>
  </Form>
  <baidu-map :center="center" id="allmap" :zoom="zoom" @ready="handler" :scroll-wheel-zoom="true" @click="getPoint" style="height : 600px">
    <bm-local-search :keyword="center" :auto-viewport="true" :location="center" v-if="this.isClose"></bm-local-search>
    <bm-marker :position="points[0]" :dragging="true" animation="BMAP_ANIMATION_BOUNCE" v-if="isShow[0]&&!this.isClose" @dragend="movea"></bm-marker>
    <bm-marker :position="points[1]" :dragging="true" animation="BMAP_ANIMATION_BOUNCE" v-if="isShow[1]&&!this.isClose" @dragend="moveb"></bm-marker>
    <bm-marker :position="points[2]" :dragging="true" animation="BMAP_ANIMATION_BOUNCE" v-if="isShow[2]&&!this.isClose" @dragend="movec"></bm-marker>
    <bm-marker :position="points[3]" :dragging="true" animation="BMAP_ANIMATION_BOUNCE" v-if="isShow[3]&&!this.isClose" @dragend="moved"></bm-marker>
  </baidu-map>
  </section>
</template>
<script>
export default {
  data () {
    return {
      center: "南通",
      zoom: 15,
      key : '',
      isShow : [
        false,
        false,
        false,
        false
      ],
      num : 0
    }
  }/*,
  watch : {
    projectScope : {
      handler : function(val,oldVal) {
        let scope = JSON.parse(this.projectScope);
        for(let i = 0;i < scope.length;i++) {
          this.$set(this.isShow,i,true);
          this.$set(this.points,i,scope[i]);
        }
        this.num = 4;
      }
    }
  }*/,
  mounted() {
    if(this.projectScope) {
      let scope = JSON.parse(this.projectScope);
      for(let i = 0;i < scope.length;i++) {
        this.$set(this.isShow,i,true);
        this.$set(this.points,i,scope[i]);
      }
      this.num = 4;
    }
  },
  methods: {
    //预读
    handler ({BMap, map}) {
      this.zoom = 15
    },
    //点击地图获取经纬度
    getPoint : function(point) {
      if(this.isClose) {
        this.$emit("setShow",{data : point,sign : '关闭地图'});
      }else {
        if(this.num == 4) {
          this.$message({
            type : 'error',
            message : '只能选择4个点'
          });
          return;
        }
        this.$set(this.points,this.num,point.point);
        this.$set(this.isShow,this.num,true);
        this.num++;
      }
    },
    //搜索地点
    search : function() {
      this.center = this.key;
    },
    movea : function(e) {
      this.$set(this.points,0,e.point);
    },
    moveb : function(e) {
      this.$set(this.points,1,e.point);
    },
    movec : function(e) {
      this.$set(this.points,2,e.point);
    },
    moved : function(e) {
      this.$set(this.points,3,e.point);
    },
    getProjedtScope : function() {
      if(this.num < 4) {
        this.$message({
          type : 'error',
          message : '请选择四个点'
        });
        return;
      }
      let projectScope = JSON.stringify(this.points);
      this.$emit("setShow",{projectScope : projectScope,sign : "设置项目范围"});
    }
  },
  props : {
    isClose : {
      type : Boolean,
      default: function() {
        return true;
      }
    },
    points : {
      type : Array,
      default: function() {
        return [
          {
            lng:'',
            lat:''
          },{
            lng:'',
            lat:''
          },{
            lng:'',
            lat:''
          },{
            lng:'',
            lat:''
          }
        ];
      }
    },
    projectScope : {
      type : String,
      default : function() {
        return '';
      }
    }
  }
}
</script>