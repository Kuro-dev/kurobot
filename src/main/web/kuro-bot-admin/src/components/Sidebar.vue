<template>
    
    <div class="sidebar">
        
        <div>
            <img src="@/assets/logo.png" alt="logo" />
            <h1>Kuro Bot</h1>
        </div>
        
        <h2>General</h2>
        
        <router-link to="/">
            <SettingsIcon></SettingsIcon>
            Configuration
        </router-link>
    
        <router-link to="/commands">
            <TerminalIcon></TerminalIcon>
            Commands
        </router-link>
    
    </div>

</template>

<script>
import {SettingsIcon, TerminalIcon} from "vue-feather-icons";
export default {
    name: "Sidebar",
    components: {SettingsIcon, TerminalIcon}
}
</script>

<style scoped>
.sidebar {
    display: flex;
    flex-direction: column;
    height: 100vh;
    background: linear-gradient(145deg, var(--color-primary-dark), var(--color-secondary-dark));
    color: white;
    font-weight: 400;
    min-width: 240px;
    box-shadow: 2px 0 16px rgba(0, 0, 0, 0.3);
}

.sidebar > div {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 10px;
}

.sidebar > div > img {
    border-radius: 32px;
    max-height: 38px;
    -webkit-user-drag: none;
    margin-right: 5px;
}

.sidebar h1 {
    margin: 10px;
    user-select: none;
    letter-spacing: 2px;
}

.sidebar h2 {
    font-size: 18px;
    margin: 10px;
    color: rgba(255, 255, 255, 0.5);
    font-weight: 400;
    user-select: none;
    letter-spacing: 0.5px;
}

.sidebar a {
    display: flex;
    align-items: center;
    color: rgba(255, 255, 255, 0.7);
    padding: 10px 12px;
    font-size: 18px;
    transition: 0.1s ease background-color, 0.1s ease border;
    cursor: pointer;
    user-select: none;
    text-decoration: none;
    border-radius: var(--border-radius);
    margin: 5px 12px;
}

.sidebar a.router-link-exact-active {
    background-color: rgba(240, 245, 255, 0.07);
    color: rgba(240, 245, 255, 1);
}

.sidebar a:hover {
    background-color: rgba(240, 245, 255, 0.1);
}

.sidebar a .feather {
    width: 21px;
    height: 21px;
    margin-right: 10px;
}
</style>