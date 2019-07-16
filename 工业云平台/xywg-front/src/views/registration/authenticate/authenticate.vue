<!--
 * @Author: zhangzhenya 
 * @CreateTime: 2019-02-22 14:28:29 
 * @Desc: 整合基本信息及企业信息 
-->
<template>
  <div>
    <information v-if="isInformation"></information>
    <enterprise v-if="isEnterprise"></enterprise>
  </div>
</template>

<script>
import information from "../information/information";
import enterprise from "../enterprise/enterprise";
export default {
  components: {
    information,
    enterprise
  },
  data() {
    return {
      isInformation: true,
      isEnterprise: false
    };
  },
  watch: {
    $route: {
      deep: true,
      handler() {
        if (this.$route.query.route == "information") {
          this.isInformation = true;
          this.isEnterprise = false;
        } else if (this.$route.query.route == "enterprise") {
          this.isInformation = false;
          this.isEnterprise = true;
        }
      }
    }
  },
  mounted() {
    if (this.$route.query.route == "information") {
      this.isInformation = true;
      this.isEnterprise = false;
    } else if (this.$route.query.route == "enterprise") {
      this.isInformation = false;
      this.isEnterprise = true;
    }
  },
  methods: {}
};
</script>

<style lang="less" scoped>
</style>
