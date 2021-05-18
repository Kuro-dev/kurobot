import Vue from "vue"
import VueRouter from "vue-router"
import Config from "../views/Config.vue"
import Commands from "../views/Commands";

Vue.use(VueRouter)

const routes = [
    {
        path: "/",
        name: "Config",
        component: Config
    },{
        path: "/commands",
        name: "Commands",
        component: Commands
    },
]

const router = new VueRouter({
    routes
})

export default router
