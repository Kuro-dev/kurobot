<template>
    <div id="app">
        
        <Sidebar></Sidebar>
        
        <router-view class="page" />
    </div>
</template>

<script>
import Sidebar from "@/components/Sidebar";
export default {
    components: {Sidebar},
}
</script>

<style>

* {
    box-sizing: border-box;
}

:root {
    --color-primary-dark: #333154;
    --color-secondary-dark: #282742;
    --color-light: #989da1;
    
    --color-green: #65c82d;
    --color-red: #cb2b2b;
    --color-blue: #2f96d0;
    --color-orange: #eeaa25;
    
    --border-radius: 8px;
    --box-shadow: 1px 1px 16px rgba(0, 0, 0, 0.1);
}

html, body, #app {
    height: 100vh;
}

#app {
    display: flex;
}

body {
    margin: 0;
    background-color: rgb(245, 248, 250);
    color: var(--color-primary-dark);
    font-family: "Open Sans", sans-serif;
}

#app > .page {
    flex-grow: 1;
    overflow: auto;
    padding: 20px 40px;
    display: flex;
    flex-direction: column;
}

#app > .page  h1 {
    margin: 0 0 30px 0;
    display: flex;
    align-items: center;
}

#app > .page h1 > .feather {
    width: 36px;
    height: 36px;
    margin-right: 20px;
    stroke-width: 2.5px;
}
</style>