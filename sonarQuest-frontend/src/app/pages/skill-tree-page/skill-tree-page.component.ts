import { Component, OnInit } from '@angular/core';
import { UserSkill } from '../../Interfaces/UserSkill';
import { SkillTreeService } from '../../services/skill-tree.service';
import { NestedTreeControl } from '@angular/cdk/tree';
import { MatTreeNestedDataSource } from '@angular/material/tree';
import * as shape from 'd3-shape';
import { NgxGraphModule } from '@swimlane/ngx-graph';
import { UserSkillGroup } from 'app/Interfaces/UserSkillGroup';
import { Router, RouterModule } from '@angular/router';
import { RoutingUrls } from 'app/app-routing/routing-urls';
import { PermissionService } from 'app/services/permission.service';


@Component({
  selector: 'app-skill-tree-page',
  templateUrl: './skill-tree-page.component.html',
  styleUrls: ['./skill-tree-page.component.css']
})
export class SkillTreePageComponent implements OnInit {

  userSkillGroupTree: { nodes: [], links: [] };
  curve = shape.curveMonotoneY
  nodecolor = '#c0c0c0';
  isAdmin = false;
  isGamemaster = false;
  // curve = shape.curveLinear;


  constructor(private skillTreeService: SkillTreeService,
    private router: Router,
    private permissionService: PermissionService) { }

  ngOnInit() {

    this.isAdmin = true && this.permissionService.isUrlVisible(RoutingUrls.admin);
    this.isGamemaster = true && this.permissionService.isUrlVisible(RoutingUrls.gamemaster);

    this.skillTreeService.userSkillGroupTree$.subscribe(userSkillGroupTree => {
      this.userSkillGroupTree = userSkillGroupTree;
    });
    this.skillTreeService.getData();
    // this.showGraph();
  }
  navigatToInnerSkilLTree(id: number) {
    this.router.navigate([RoutingUrls.innerskilltree, id]);
    console.log(RoutingUrls.innerskilltree);
  }

  /*showGraph() {
    
    console.log("####### Data from Server#####");
    console.log(this.userSkillGroupTree)
  
  }*/

  getSkillGroupIcon(icon: String): String {
    switch (icon) {
      case 'ra-shield': {
        return 'M511.992 928.008c49.995-48.013 200.006-143.987 299.997-143.987 0-48.013 0-143.987 0-192 0-192-150.011-480-299.997-623.987-150.011 143.987-299.997 431.987-299.997 623.987 0 48.013 0 143.987 0 192 100.016-0.025 250.002 95.975 299.997 143.987v0z'

      }
      case 'ra-fairy-wand': {
        return 'M826.801 644.087l93.146 211.044-211.046-93.146-171.932 153.802 23.373-229.498-199.404-115.988 172.493-37.247c-16.95-15.52-34.714-33.475-53.009-51.768v-0.003l-462.031-456.829v-65.269h90.566l450.044 443.511c16.416 16.417 30.864 31.602 43.247 45.647l32.87-152.22 115.988 199.404 229.498-23.373-153.802 171.932z'

      }
      case 'ra-x-mark':{
       return 'M836.968 327.119c-10.041 4.297-31.777-7.131-57.498-26.575l-147.463 147.463 147.474 147.474c25.715-19.439 47.447-30.864 57.486-26.567 35.708 15.303 173.402 336.64 152.998 357.046-20.405 20.423-341.742-117.29-357.045-152.998-4.302-10.038 7.118-31.766 26.554-57.476l-147.474-147.473-147.473 147.473c19.436 25.712 30.856 47.439 26.554 57.476-15.303 35.708-336.638 173.421-357.043 152.998-20.405-20.406 117.29-341.742 152.998-357.046 10.039-4.297 31.771 7.128 57.486 26.567l147.474-147.474-147.463-147.463c-25.72 19.444-47.457 30.872-57.498 26.575-35.709-15.303-173.402-336.64-152.998-357.045 20.405-20.425 341.742 117.288 357.045 152.998 4.302 10.036-7.114 31.759-26.545 57.464l147.463 147.464 147.464-147.464c-19.429-25.705-30.845-47.429-26.545-57.464 15.303-35.709 336.64-173.422 357.045-152.998 20.406 20.405-117.288 341.742-152.996 357.045z'

      }
      case 'ra-diamond':{
        return 'M854.688 770.722h-687.528l-145.385-195.843 491.002-538.081 491.002 538.081-149.091 195.843zM512.778 42.528l-192.666 528.745 192.664 196.661 192.664-196.661-192.662-528.745zM305.311 581.705l-148.246 195.664zM716.815 581.705l145.914 195.664zM305.311 571.627h411.504v-18.375h-411.504v18.375z'

      }
      case 'ra-leaf':{
        return 'M898.619 654.923c-1.848-5.569-3.742-11.172-5.638-16.807 21.572-105.467 13.594-220.065-40.816-345.175 7.896 80.356 6.59 157.217-5.828 230.894-14.657-28.517-33.349-56.146-52.473-83.961 2.713-79.902-12.176-164.233-51.31-254.216 6.278 63.909 5.128 125.514-1.164 185.418-25.628-29.141-54.235-57.15-86.293-82.797-7.591-50.672-21.875-103.145-45.482-157.43 4.235 43.137 4.915 85.749 3.496 127.106-71.065-48.332-155.463-86.983-257.712-108.447 86.103 48.325 154.818 98.182 211.068 149.265-37.003 7.709-75.076 12.777-113.113 16.326 50.288 12.707 99.963 17.665 148.101 16.326 34.398 35.177 63.139 70.557 86.293 107.284-64.163 11.538-129.101 14.874-194.745 10.492 75.37 36.473 150.451 49.212 225.061 44.313 19.277 39.682 32.454 80.729 41.981 122.441-56.743 5.362-118.52-8.102-188.914-25.655 63.562 58.228 132.266 83.52 200.574 90.959 3.553 28.903 5.422 57.86 5.76 87.718-315.121 224.233-231.651-529.919-725.274-651.082 406.167-251.603 1192.676 113.245 846.43 537.029zM825.227 900.939c20.091 9.774 44.48 10.349 62.972-6.996 10.554-99.306-21.77-309.203-149.265-455.959 97.235 177.214 104.361 305.538 86.293 462.952v0.003z'

      }
      case 'ra-book':{
        return 'M983.293 364.413l-67.594 22.31 1.591-1.665-373.85-135.67-357.881 378.335-87.855 28.998c-44.338-31.379-49.323-120.782-19.511-151.87l441.774-454.079 467.257 167.304c-42.553 23.261-77.1 89.476-3.93 146.337zM495.156 241.88l-368.136 381.698h11.020l366.51-374.19-9.393-7.508zM523.302 64.41c0 0-50.929 59.504 15.072 138.695l400.039 144.866c0 0-46.001-78.994 7.832-125.649l-422.944-157.912zM185.558 627.724l-12.216 12.914 372.938 132.431 369.42-386.344 67.592-22.31c-0.33-0.256-0.641-0.514-0.967-0.771l7.884 2.814-435.941 452.001-454.798-162.317 86.088-28.416z'

      }
      case 'ra-forging':{
        return 'M733.871 172.577h127.091v58.189c39.435 13.968 78.873 39.009 118.31 73.31-39.562 34.993-78.332 54.504-118.31 63.856v59.399h-466.406v-40.606h-177.566c31.939-77.495 102.326-130.671 177.566-148.345v-65.803h116.97c-27.8-70.080-80.057-125.917-139.53-174.092h511.531c-70.902 48.145-123.109 103.925-149.656 174.092zM475.368 461.868l98.423 44.313-42.61 140.473-111.452 109.498-63.882-28.762 33.705-111.135-355.031-159.819 25.049-55.643 355.052 159.828zM721.492 465.203l253.671 101.717-141.387 3.927 95.381 137.166-146.676-78.414 42.894 190.828-152.574-242.71-16.506 65.275-60.353-177.653 125.154 58.644z';

      }
      case 'ra-tower':{
        return 'M905.017-20.080l-57.307 185.374h-78.581v376.693l122.113 151.378v221.879h-128.636v-104.655h-78.302v104.655h-130.035v-104.655h-76.9v104.655h-130.035v-104.655h-76.902v104.655h-128.648v-221.879l122.113-151.378v-376.693h-78.579l-57.307-185.374z'

      }
      case'ra-telescope':{
        return 'M942.731 328.605c-13.755 13.755-31.48 22.192-51.676 25.685l-234.209 188.931c-19.359 19.357-48.885 24.977-81.623 18.594-2.718 7.754-6.884 14.662-12.609 20.386l-107.233 86.499 0.844 0.844c-3.899 3.899-8.418 6.966-13.399 9.284l-48.295 38.957c-14.388 14.388-37.627 16.805-62.626 9.042-1.179 7.257-4.138 13.624-9.034 18.521l-92.893 74.937c11.545 29.222 10.23 57.222-6.37 73.822-27.007 27.005-84.182 13.613-127.708-29.913s-56.92-100.703-29.913-127.708c16.556-16.554 44.45-17.904 73.586-6.461l74.996-92.972c4.898-4.899 11.269-7.858 18.527-9.037-7.763-24.999-5.346-48.238 9.041-62.624l43.432-53.841c1.555-2.327 3.306-4.52 5.282-6.548l86.871-107.69c5.723-5.725 12.629-9.891 20.383-12.609-6.383-32.74-0.763-62.267 18.594-81.626l188.941-234.225c3.495-20.188 11.931-37.905 25.682-51.654 53.355-53.355 166.316-26.896 252.31 59.098s112.451 198.955 59.098 252.31zM740.997 159.219c37.536 37.536 85.941 51.008 112.921 32.897 1.3-3.102 2.279-6.396 2.942-9.851-8.838-3.595-17.877-9.567-25.961-17.65-21.451-21.451-28.051-49.628-14.742-62.938 2.666-2.666 5.935-4.519 9.62-5.628-2.756-3.216-5.66-6.381-8.745-9.468-37.536-37.536-85.941-51.010-112.921-32.897-11.65 27.812 2.571 71.221 36.886 105.535z'
      }
      case 'ra-crystal-ball':{
        return 'M838.581 247.993c0.545 3.378 0.84 6.785 0.84 10.223 0 33.232-25.899 63.829-69.381 88.203 126.174 138.886 122.218 353.843-11.887 487.949-138.192 138.192-362.242 138.192-500.434 0-134.107-134.107-138.061-349.065-11.884-487.952-43.479-24.374-69.378-54.969-69.378-88.199 0-4.3 0.459-8.553 1.308-12.757-50.308-30.127-80.042-67.333-80.042-107.605 0-100.399 184.728-181.791 412.607-181.791s412.607 81.389 412.607 181.791c0 41.409-31.438 79.578-84.356 110.139zM283.995 807.234c123.735 123.734 324.346 123.734 448.079 0s123.735-324.344 0-448.079c-61.497-61.497-141.982-92.414-222.579-92.783-53.004 0.155-105.978 13.516-153.553 40.106-0.603-0.107-1.198-0.221-1.799-0.329-25.146 13.984-48.793 31.65-70.148 53.006-123.734 123.734-123.735 324.346 0 448.079zM751.527 667.355c-28.827-28.827-88.791-15.601-133.933 29.542s-58.368 105.106-29.542 133.933c28.827 28.827 88.791 15.601 133.933-29.542s58.368-105.106 29.542-133.933zM557.428 311.625c-57.675-57.675-177.649-31.211-267.97 59.109s-116.784 210.295-59.109 267.97c57.675 57.675 177.649 31.211 267.97-59.109s116.784-210.295 59.109-267.97z'

      }
      case 'ra-broken-bone':{
        return 'M668.46 393.021l-103.896 54.414 11.551-69.274-79.982 27.223c140.578-98.745 235.983-223.089 323.239-380.963 45.236-81.824 143.575-12.893 68.432 68.432 82.667-67.838 155.938 49.888 81.637 81.637-127.123 54.321-235.702 125.187-300.98 218.531v0zM33.595 349.408c-27.535-41.24 16.546-101.867 108.485-66.621-110.858-66.527-16.515-162.119 50.137-86.851 68.369 77.173 149.663 148.82 235.109 182.848l25.35 56.381-65.497-12.519 96.716 119.412c-119.068-83.822-248.314-149.725-397.758-162.338-26.349-2.217-43.363-16.577-52.541-30.313zM566.281 550.645l86.944 207.293-51.261 161.432-66.277-269.075 30.594-99.65zM843.254 374.883l-240.322 123.283 223.464-17.202 16.858-106.081zM507.247 579.303l-185.127 293.925 4.058-221.029 181.069-72.896zM457.359 353.904l-120.317-158.279 93.375 20.573 26.942 137.706zM623.287 539.156l170.267 77.891-111.295 3.122-58.972-81.013zM495.196 352.062l37.931-236.389 72.709 39.18-110.639 197.209z'

      }
      default: {
       return 'M996.95 353.994c0 211.676-70.023 457.178-248.753 572.707l-0.002 0.003-47.112-72.874c45.387-27.92 69.211-53.788 92.22-99.495-15.92-22.032-27.83-48.668-37.831-78.708 8.142-18.115 14.020-50.442 14.045-71.849l-71.682 11.698-61.884-67.948-49.75 101.921c-65.452-80.468-85.069-114.28-186.263-107.385l-28.604 88.231-102.014-19.763c0 0-18.594 101.623-49.829 124.382 8.768 36.649 34.876 69.076 82.515 98.282l-45.253 83.798c-165.621-116.125-232.981-346.879-232.981-563.001 185.518-93.299 240.866-199.324 230.554-311.836 27.66-15.575 56.27-31.047 89.795-40.040 45.851-12.3 95.033-18.206 145.612-18.2 50.579 0.008 102.377 5.9 150.466 18.2 43.541 11.139 83.051 28.537 118.916 49.75-1.604 109.456 58.064 217.606 237.835 302.127zM375.055 297.686c-56.42-32.576-122.325-24.237-146.721 18.016-13.925 24.115-10.9 54.025 4.291 81.507l-0.002 0.002c25.628 0.438 55.842 5.586 88.376 18.019 30.994 11.839 60.045 28.061 83.228 45.472 11.982-6.298 22.424-15.768 29.173-27.454 24.396-42.256-1.926-102.986-58.346-135.562zM429.429 93.285c19.698 72.138 43.382 144.274 80.929 216.411 0 0.002-0.002 0.002-0.002 0.003h0.003c0-0.002-0.002-0.002-0.002-0.003 37.003-72.138 66.358-144.274 80.929-216.411-53.955 16.459-107.906 18.654-161.859 0zM792.215 315.7c-24.397-42.251-90.302-50.592-146.721-18.016s-82.742 93.306-58.345 135.562c6.749 11.687 17.191 21.157 29.173 27.454 23.181-17.411 52.234-33.633 83.228-45.472 32.536-12.433 62.748-17.582 88.376-18.019l-0.002-0.002c15.19-27.483 18.215-57.391 4.29-81.507zM210.688 723.473c10.564 27.87 17.744 61.627 51.873 83.574zM804.393 744.075c-11.623 22.029-30.887 60.496-54.038 77.777z'
      }
    }
  }
}
